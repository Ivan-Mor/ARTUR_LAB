package org.jdbc_lab.utils;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Класс InputManager предназначен для обработки входной информации
 */

public class InputManager {
    // Scanner обязан быть единственным на весь ввод: если создавать его заново перед каждым
    // чтением, то после nextInt() непрочитанный перевод строки "\n" в потоке достаётся следующему
    // nextLine(), и тот вместо ввода пользователя возвращает пустую строку
    private static final Scanner SCANNER = new Scanner(System.in);

    public static int getNextInt() {
        while (true) {
            try {
                return SCANNER.nextInt();
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.print("Необходимо целое. Попробуй снова: ");
            } catch (NoSuchElementException e) {
                // поток ввода закончился (например, Ctrl+D) — выходим из приложения
                System.exit(0);
            }
        }
    }

    public static int getNextIntInRange(int min, int max) {
        while (true) {
            int input = getNextInt();

            if (input < min || input > max) {
                System.out.print("Введённое находится за пределами. Попробуй снова: ");
            } else {
                return input;
            }
        }
    }

    public static long getNextLong() {
        while (true) {
            try {
                return SCANNER.nextLong();
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.print("Необходимо длинное целое. Попробуй снова: ");
            } catch (NoSuchElementException e) {
                System.exit(0);
            }
        }
    }

    public static double getNextDouble() {
        while (true) {
            try {
                return SCANNER.nextDouble();
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.print("Необходимо вещественное. Попробуй снова: ");
            } catch (NoSuchElementException e) {
                System.exit(0);
            }
        }
    }

    public static String getNextLine() {
        while (true) {
            try {
                // после nextInt()/nextLong() в потоке остаётся "\n" — пропускаем его
                String line = SCANNER.nextLine();
                if (line.isEmpty()) {
                    continue;
                }
                return line;
            } catch (NoSuchElementException e) {
                // поток ввода закончился (например, Ctrl+D) — выходим из приложения
                System.exit(0);
                return "";
            }
        }
    }

    // При вводе "-" возвращает пустую строку
    public static String getNextLineWithSkip() {
        String line = getNextLine();

        if (line.equals("-")) {
            return "";
        }

        return line;
    }
}
