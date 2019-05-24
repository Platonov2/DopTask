package com.company;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Stack<List<Character>> tags = new Stack<>();
        int symbol;
        Integer offset = 0;
        List<Character> line = new ArrayList<>();
        boolean pre = false;
        Character[] temp1 = {'[', 'p', 'r', 'e', ']'};
        Character[] temp2 = {'[', '/', 'p', 'r', 'e', ']'};
        List<Character> preStart = Arrays.asList(temp1);
        List<Character> preEnd = Arrays.asList(temp2);

        try (FileInputStream stream = new FileInputStream("C:\\Users\\student\\Desktop\\slackbot-master\\DopTask\\src\\com\\company\\text.txt")) {
            while ((symbol = stream.read()) != -1) {
                switch (symbol) {
                    case (' '):
                        if (!pre && (line.size() == 0 || line.get(line.size() - 1) == ' '))
                            break;

                        line.add((char) symbol);
                        break;
                    case ('['):
                        printLine(offset, line);
                        line.clear();
                        line.add((char) symbol);
                        break;
                    case (']'):
                        line.add((char) symbol);
                        checkTag(line, tags);

                        if (line.equals(preEnd)) pre = false;
                        else if (line.equals(preStart)) pre = true;

                        if (line.get(1) == '/') {
                            offset -= 1;
                            printLine(offset, line);
                        } else {
                            printLine(offset, line);
                            offset += 1;
                        }

                        line.clear();
                        break;
                    case ('\r'):
                        if (pre) {
                            line.add((char) symbol);
                            line.add((char) stream.read());
                            break;
                        }

                        stream.read();
                        break;
                    default:
                        line.add((char) symbol);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printLine(Integer offset, List<Character> line) {
        printOffset(offset);
        for (Character symbol : line) {
            System.out.print(symbol);
        }
    }

    public static void printOffset(Integer offset) {
        System.out.println();
        for (int i = 0; i < offset; i++) {
            System.out.print('\t');
        }
    }

    public static void checkTag(List<Character> tag, Stack<List<Character>> tags) {
        if (tag.contains('/')) {
            List<Character> last = tags.peek();

            for (int i = 2; i < tag.size(); i++) {
                if (last.get(i - 1) == ' ')
                    break;
                if (tag.get(i) != last.get(i - 1))
                    throw new IllegalArgumentException("Неверные входные данные");
            }
            tags.pop();
        } else {
            tags.add(new ArrayList<>(tag));
        }
    }
}