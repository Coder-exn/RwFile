package com.hzy.rwfile;

import java.io.*;
import java.util.Arrays;

/**
 * RwFile is a class for users to perform IO operations more conveniently.
 * JUST LIKE PYTHON!
 *
 * @author hzy
 * @since 3/27/2021
 */
public class RwFile implements AutoCloseable {

    /**
     * When user open an RwFile, it'll be initialized.
     *
     * file = new File(path);
     * The path is what the user want.
     */
    private final File file;

    /**
     * The InputStream object is for reading a file.
     *
     * When mode 'r' is enabled, it will be initialized.
     *
     * @see InputStream
     */
    private InputStream inputStream;

    /**
     * The OutputStream object is for writing a file.
     *
     * When mode 'w' or 'a' is enabled, it will be initialized.
     *
     * @see OutputStream
     */
    private OutputStream outputStream;

    /**
     * The reader of field inputStream (buffered).
     *
     * @see #inputStream
     * @see BufferedWriter
     */
    private BufferedReader reader;

    /**
     * The writer of field inputStream (buffered).
     *
     * @see #outputStream
     * @see BufferedWriter
     */
    private BufferedWriter writer;

    /**
     * Create a new RwFile instance.
     * When file not exists, it will create file and directories automatically. If it cannot create the file, it'll throw this exception.
     *
     * @param path A pathname string
     * @param mode The RW mode for file reading or writing
     * @throws IOException Exceptions of IO operations
     * @throws RwFileCreateException If it cannot create the file, it'll throw this exception.
     *
     * @see #open(String, String)
     */
    public RwFile(String path, String mode) throws IOException {
        this.file = new File(path);
        if (mode.contains(RwMode.READ.getValue())) {
            createFileAndDirs(file);
            this.inputStream = new FileInputStream(path);
            this.reader = new BufferedReader(new InputStreamReader(inputStream));
        }
        if (mode.contains(RwMode.WRITE.getValue()) && mode.contains(RwMode.APPEND.getValue())) {
            throw new UnsupportedRwModeException("Mode 'r' and 'w' cannot exist side by side!");
        } else if (mode.contains(RwMode.WRITE.getValue())) {
            this.outputStream = new FileOutputStream(path);
            this.writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        } else if (mode.contains(RwMode.APPEND.getValue())) {
            this.outputStream = new FileOutputStream(path, true);
            this.writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        }
    }

    /**
     * Factory method for RwFile.
     * It will create a new RwFile instance.
     *
     * @param path A pathname string.
     * @param mode Mode for IO operations.
     * @return A new RwFile instance.
     * @throws IOException Exceptions of IO operations.
     * @throws RwFileCreateException Thrown when it cannot create file or directories.
     * @see #RwFile(String, String)
     */
    public static RwFile open(String path, String mode) throws IOException {
        if (path == null || mode == null) {
            throw new RwFileConstructException("Arguments for RwFile must be non-null!");
        }
        return new RwFile(path, mode);
    }

    /**
     * It create your file and directories. If your file is not exists, it'll work.
     *
     * @param file The file object
     * @throws IOException Exceptions of IO operations.
     * @throws RwFileCreateException If the method cannot create the file or directories, it'll throw the exception.
     */
    private void createFileAndDirs(File file) throws IOException {
        if (file.exists()) {
            return;
        }
        String[] paths = file.getAbsolutePath().replace(File.separator, "/").split("/");
        paths = Arrays.copyOf(paths, paths.length - 1);
        StringBuilder sb = new StringBuilder();
        for (String i : paths) {
            sb.append(i).append(File.separator);
        }
        if (!new File(sb.toString()).mkdirs() || !file.createNewFile()) {
            throw new RwFileCreateException("Cannot create file, please create file and directories manually.");
        }
    }

    /**
     * It reads a int-type data and returns.
     *
     * @return The next character data (int-type)
     * @throws IOException Exceptions of IO operations.
     * @throws UnsupportedRwModeException Thrown when 'r' mode isn't enabled.
     */
    public int readOne() throws IOException {
        requireInputStreamNonNull();
        return inputStream.read();
    }

    /**
     * It reads a char-type data and returns.
     *
     * @return The next character data (char-type)
     * @throws IOException See: {@link #readOne()}
     * @throws UnsupportedRwModeException See: {@link #readOne()}
     * @see #readOne()
     */
    public char readOneChar() throws IOException {
        return (char) readOne();
    }

    /**
     * It reads all content of the file.
     *
     * @return String of the file content.
     * @throws IOException Exceptions of IO operations.
     * @throws UnsupportedRwModeException Thrown when 'r' mode isn't enabled.
     * @see #inputStream
     */
    public String readContent() throws IOException {
        requireInputStreamNonNull();
        StringBuilder fileContent = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            fileContent.append(line);
        }
        return fileContent.toString();
    }

    /**
     * It reads a line of data of the file.
     *
     * @return A line of data of the file.
     * @throws UnsupportedRwModeException Thrown when 'r' mode isn't enabled.
     * @throws IOException Exceptions of IO operations.
     * @see #inputStream
     */
    public String readLine() throws IOException {
        requireInputStreamNonNull();
        return reader.readLine();
    }

    /**
     * Write a string to the file.
     *
     * @param content The content what you want to write.
     * @throws IOException Exceptions of IO operations.
     * @throws UnsupportedRwModeException Thrown when 'r' mode isn't enabled.
     * @see #inputStream
     */
    public void write(String content) throws IOException {
        requireOutputStreamNonNull();
        writer.write(content);
        writer.flush();
    }

    /**
     * Write a line of string to the file.
     *
     * @param content Content of the line.
     * @throws IOException Exceptions of IO operations.
     */
    public void writeLine(String content) throws IOException {
        requireOutputStreamNonNull();
        writer.write(content + '\n');
        writer.flush();
    }

    /**
     * Close the IO resources.
     *
     * @throws IOException Exceptions of IO operations.
     */
    @Override
    public void close() throws IOException {
        closeInputStream();
        closeOutputStream();
    }

    /**
     * Close {@link #inputStream}
     * It is a tool method for {@link #close()}
     *
     * @throws IOException Exceptions of IO operations.
     * @see #close()
     */
    private void closeInputStream() throws IOException {
        if (inputStream != null) {
            inputStream.close();
            reader.close();
        }
    }

    /**
     * Close {@link #outputStream}
     * It is a tool method for {@link #close()}
     *
     * @throws IOException Exceptions of IO operations
     * @see #close()
     */
    private void closeOutputStream() throws IOException {
        if (outputStream != null) {
            outputStream.close();
            writer.close();
        }
    }

    /**
     * Check is {@link #inputStream} null
     *
     * @throws UnsupportedRwModeException Thrown when it's null
     */
    private void requireInputStreamNonNull() throws UnsupportedRwModeException {
        if (inputStream == null) {
            throw new UnsupportedRwModeException("Mode 'r' is not supported.");
        }
    }

    /**
     * Throw exception if {@link #outputStream} is null
     *
     * @throws UnsupportedRwModeException Thrown when it's null
     * @see #outputStream
     */
    private void requireOutputStreamNonNull() throws UnsupportedRwModeException {
        if (outputStream == null) {
            throw new UnsupportedRwModeException("Mode 'w' is not supported.");
        }
    }

    /**
     * A method for user to enable the 'r' mode.
     *
     * @throws IOException Exceptions of IO operations
     * @throws RwFileCreateException Thrown when it cannot create the file or directories
     */
    public void enableRead() throws IOException {
        if (inputStream == null) {
            createFileAndDirs(file);
            inputStream = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(inputStream));
        }
    }

    /**
     * A method for user to enable the 'w' mode.
     *
     * @param appendable Is the file appendable ('a' mode)
     * @throws IOException Exceptions of IO operations
     */
    public void enableWrite(boolean appendable) throws IOException {
        if (outputStream == null) {
            outputStream = new FileOutputStream(file, appendable);
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        }
    }

    /**
     * Enable many modes.
     * Enable modes at once, with only one method.
     *
     * @param modes Modes of RwFile
     * @throws IOException Exceptions of IO operations
     */
    public void enableModes(RwMode... modes) throws IOException {
        for (RwMode mode : modes) {
            if (mode == RwMode.READ) {
                enableRead();
            } else {
                enableWrite(mode == RwMode.APPEND);
            }
        }
    }

    /**
     * @return The file object.
     */
    public File getFile() {
        return file;
    }

    /**
     * @return The {@link #inputStream} object
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * @return The {@link #inputStream} object
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * @return The initial path of the file
     */
    public String getPath() {
        return file.getPath();
    }

    /**
     * @return The absolute path of the file
     */
    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }
}
