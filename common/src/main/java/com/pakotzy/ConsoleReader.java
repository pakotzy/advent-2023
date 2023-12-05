package com.pakotzy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

public class ConsoleReader {
    private ConsoleReader() {}

    public static List<String> readToString(String[] args, InputStream in) throws IOException {
        InputStream inputStream = determineInput(args, in);
        return readStream(inputStream);
    }

    private static InputStream determineInput(String[] args, InputStream in) {
        return args.length > 0 ? new ByteArrayInputStream(args[0].getBytes()) : in;
    }

    private static List<String> readStream(InputStream inputStream) throws IOException {
        List<String> content = new ArrayList<>();
        StringBuilder lineContent = new StringBuilder();
        try (ReadableByteChannel channel = Channels.newChannel(inputStream)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (channel.isOpen() && channel.read(buffer) > 0) {
                buffer.flip();

                while (buffer.hasRemaining()) {
                    byte curByte = buffer.get();
                    if (curByte == '\n') {
                        if (!lineContent.isEmpty()) {
                            content.add(lineContent.toString());
                            lineContent = new StringBuilder();
                        } else if (!buffer.hasRemaining()) {
                            throw new ClosedByInterruptException();
                        }
                    } else {
                        lineContent.append(Character.toString(curByte));
                    }
                }

                buffer.clear();
            }
        } catch (ClosedByInterruptException ignored) { /*user interrupted the input */ }

        if (!lineContent.isEmpty()) {
            content.add(lineContent.toString());
        }

        return content;
    }
}
