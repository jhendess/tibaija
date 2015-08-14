/*
 * Copyright (c) 2015 Jakob Hendeß
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE
 */

package org.xlrnet.tibaija;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Class with file system access for loading new programs from disk.
 */
public class FileSystemCodeProvider implements CodeProvider {

    private static final String DEFAULT_FILE_EXTENSION = ".tib";

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemCodeProvider.class);

    private Path defaultPath;

    private Map<String, String> registeredFiles = new HashMap<>();

    public FileSystemCodeProvider(@NotNull Path defaultPath) {
        LOGGER.debug("Initialized default path '{}' for new programs", defaultPath);

        checkArgument(Files.isDirectory(defaultPath), "Path must be directory");
        this.defaultPath = defaultPath;
    }

    @NotNull
    public static String stripFilename(@NotNull String filename) {
        if (filename.contains("."))
            return StringUtils.substringBeforeLast(filename, ".").toUpperCase();
        return filename.toUpperCase();
    }

    @NotNull
    public static String stripFilename(@NotNull Path filepath) {
        return stripFilename(filepath.toFile().getName());
    }

    @Override
    @NotNull
    public String getProgramCode(String programName) throws IOException {
        String strippedName = stripFilename(programName);
        if (registeredFiles.containsKey(programName)) {
            LOGGER.debug("Found loaded program {}", programName);
            return registeredFiles.get(programName);
        }

        String fileName = strippedName + DEFAULT_FILE_EXTENSION;

        Path path = Paths.get(defaultPath.toString(), fileName);
        LOGGER.debug("Trying to load file '{}'", path.toString());

        if (Files.exists(path)) {
            return loadFileContent(path);
        } else {
            throw new FileNotFoundException(path.toAbsolutePath().toString());
        }
    }

    /**
     * Register a file ({@link Path} object) under a custom filename.
     *
     * @param filepath
     *         Actual file to store.
     * @return The internal name under which the file will be known.
     * @throws IOException
     */
    public String registerFile(@NotNull Path filepath) throws IOException {
        checkArgument(!Files.isDirectory(filepath), "File may not be a directory");

        String internalFilename = stripFilename(filepath);
        checkArgument(!registeredFiles.containsKey(internalFilename), "File with name %s is already loaded and cannot be registered", internalFilename);

        registeredFiles.put(internalFilename, loadFileContent(filepath));
        LOGGER.info("Registered path '{}' as internal file '{}'", filepath, internalFilename);

        return internalFilename;
    }

    @NotNull
    private String loadFileContent(@NotNull Path filepath) throws IOException {
        checkArgument(!Files.isDirectory(filepath), "Path must be a file");

        return Files.lines(filepath).collect(Collectors.joining("\n"));
    }


}
