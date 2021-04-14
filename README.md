# RwFile

RwFile is a file object for users to operate files more conveniently.

Use `RwFile`, you needn't think about InputStream, OutputStream and creating files. `RwFile` will help you with all these problems!

**JUST LIKE PYTHON!**

Okay, let's start to learn how to use it!

Firstly, we should know about the file `/hello.txt` and its content:

```
Hello world!
```

You can add a dependency in your `pom.xml` (Maven):

**Warning: I'm sorry that you can't add the maven dependency now. I'm new to maven and github, about maven, there's still some problems that I have to resolve. You can use the jars for the time being. If you have any ideas, please propose (contact me at: 787305742@qq.com)**

```xml
<dependency>
    <groupId>io.github.coder-exn</artifactId>
    <artifactId>rwfile</artifactId>
    <version>1.0-RELEASE</version>
</dependency>
```

First, you may open a file:

```java
public static void main(String[] args) throws IOException {
    try (RwFile file = RwFile.open("/hello.txt", "rw")) {
    	...
    }
}
```

The first parameter is the path of the file, and the second parameter is the mode.

You can understand the modes with the table:

|                  | Mode pattern | Meaning                      |
| ---------------- | ------------ | ---------------------------- |
| Read             | r            | Read from the file.          |
| Write            | w            | Write and override the file. |
| Write+appendable | a            | Append after the file.       |

If you want to read, you can write your code like this: (`r` mode for reading)

```java
try (RwFile file = RwFile.open("/hello.txt", "r")) {
    System.out.println(file.readContent());
}
```

Result:

```
Hello world!
```

It works! For reading the file, it has three methods:

| Method name   | Method's work                                                |
| ------------- | ------------------------------------------------------------ |
| readOne()     | Read a int-type data from the file (Same as inputStream.read()) |
| readOneChar() | Read a char-type data from the file                          |
| readContent() | Read the string of all the content.                          |
| readLine()    | Read a line of string data from the file.                    |

The methods all use `BufferedReader`. If there's any shortcoming, welcome to propose your advice!

`w` mode for writing:

```java
try (RwFile file = RwFile.open("/hello.txt", "w")) {
    file.write("Hello Mike!");
}
```

After the program exits, the file content will be:

```
Hello Mike!
```

You can also use the `a` mode (append):

```java
try (RwFile file = RwFile.open("/hello.txt", "a")) {
    file.write("Hello ABC!~");
}
```

File content:

```
Hello Mike!Hello ABC!~
```

Here are two methods for writing:

| Method name       | Method's work                                        |
| ----------------- | ---------------------------------------------------- |
| write(String)     | Write a string data to the file.                     |
| writeLine(String) | Write a line of string data (with '\n') to the file. |

##### UnsupportedRwModeException

It'll be thrown when you operate the file but didn't open the mode:

```java
try (RwFile file = RwFile.open("/hello.txt", "w")) {
    System.out.println(file.readContent());
}
```

Result:

```
Exception in thread "main" UnsupportedRwModeException: Mode 'r' is not supported.
	at RwFile.requireInputStreamNonNull(RwFile.java:253)
	at RwFile.readContent(RwFile.java:159)
	at lab.Main.main(Main.java:10)
```

But don't worry! If you need, you can do this:

```java
try (RwFile file = RwFile.open("/hello.txt", "w")) {
    file.enableRead();
    System.out.println(file.readContent());
}
```

Result:

```
Hello Mike!Hello ABC!~
```

You can also do this:

```java
try (RwFile file = RwFile.open("/hello.txt", "r")) {
    file.enableWrite(false);
    file.write("Hello world!");
    System.out.println(file.readContent());
}
```

Result:

```
Hello world!
```

The parameter `false` is "`appendable`". If you do this then:

```java
try (RwFile file = RwFile.open("/hello.txt", "r")) {
    file.enableWrite(true);
    file.write("Hello ABCDEFG!~");
    System.out.println(file.readContent());
}
```

Result:

```
Hello world!Hello ABCDEFG!~
```

| Method name          | Method's work                                                |
| -------------------- | ------------------------------------------------------------ |
| enableRead()         | Enable the 'r' mode for reading.                             |
| enableWrite(boolean) | Enable the 'w' or 'a' mode for writing. (Parameter is `appendable`) |

You can also use `enableModes`:

See also: `io.github.coderexn.RwMode`.

```java
try (RwFile file = RwFile.open("/hello.txt", "w")) {
    file.enableModes(RwMode.READ, RwMode.WRITE);
    System.out.println(file.readContent());
}
```

RwMode is an enum for reading and writing modes.

