package Arithmetic;

import java.util.*;

/**
 * Class to determine if a combination of additions and multiplications on a
 * given set
 * of numbers can result in a specified number, and what those operations are.
 *
 * @author Matthew Jennings
 */
public class App {

    public static String input;
    public static ArrayList<Integer> numbers;
    public static int target;
    public static int first;

    /**
     * Reads lines from stdin and sends them off to be checked and formatted
     *
     * @param args Not used.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            input = scanner.nextLine();
            Scanner lineScan = new Scanner(input);
            String type = lineScan.next();
            if (type.length() != 1) {
                error(input);
                continue;
            }
            if (!convertToAL(lineScan)) {
                continue;
            }
            if (type.charAt(0) == 'N') {
                outputFormat(normalOrder(), true);
            } else if (type.charAt(0) == 'L') {
                outputFormat(leftToRight(), false);
            } else {
                error(input);
                continue;
            }
        }
    }

    /**
     * Attempts to create a valid set of operations to acheive the target value by
     * evaluating
     * from left to right.
     *
     * @return String of +* representing the operations and their order.
     */
    public static String leftToRight() {
        Stack<String> stack = new Stack<String>();
        String current;
        stack.push("+");
        stack.push("*");
        first = numbers.get(0);
        numbers.remove(0); // numbers and the operations string should now have matched indices.
        while (!stack.isEmpty()) {
            current = stack.pop();
            int eval = first;
            int i;
            for (i = 0; i < current.length(); i++) {
                if (current.charAt(i) == '*') {
                    eval = eval * numbers.get(i);
                } else if (current.charAt(i) == '+') {
                    eval = eval + numbers.get(i);
                }
            }
            if (eval == target && i == numbers.size()) { // this String represents a solution.
                return current;
            }
            if (i < numbers.size() && eval * numbers.get(i) <= target) { // if the next step would not cause the number
                                                                         // to exceed the target
                stack.push(current + "*"); // push it onto the stack.
            }
            if (i < numbers.size() && eval + numbers.get(i) <= target) {
                stack.push(current + "+");
            }

        }
        return "X";

    }

    /**
     * Attempts to create a valid set of operations to acheive the target value by
     * evaluating
     * in standard order of operations.
     *
     * @return String of +* representing the operations and their order.
     */
    public static String normalOrder() {
        Stack<String> stack = new Stack<String>();
        String current;
        stack.push("+");
        stack.push("*");
        first = numbers.get(0);
        numbers.remove(0); // numbers and the operations string should now have matched indices.
        while (!stack.isEmpty()) {
            current = stack.pop();
            int eval = evaluateN(current);
            if (eval == target && current.length() == numbers.size()) { // this String represents a solution.
                return current;
            }
            if (current.length() < numbers.size() && evaluateN(current + "*") <= target) {
                stack.push(current + "*");
            }
            if (current.length() < numbers.size() && evaluateN(current + "+") <= target) {
                stack.push(current + "+");
            }

        }
        return "X";

    }

    /**
     * Evaluates a given set of operations in the normal multiplication>addition
     * manner.
     *
     * @param operations the operations to attempt
     * @return int the result of performing these operations on the static numbers.
     */
    public static int evaluateN(String operations) {
        int result = 0;
        int temp = first;
        for (int i = 0; i < operations.length(); i++) {
            if (operations.charAt(i) == '*') {
                temp = temp * numbers.get(i);
            } else {
                result += temp;
                temp = numbers.get(i);
            }
        }
        result += temp;
        // System.out.println("Evaluating " + operations + " result = " + result);
        return result;
    }

    /**
     * Converts a line into usable data stored in static variables. Extracts the
     * target value and
     * all of the numbers to be used to get there.
     *
     * @param inscan Scanner containing the remainder of the line after the type has
     *               been removed.
     */
    public static boolean convertToAL(Scanner inscan) {
        numbers = new ArrayList<Integer>();
        try {
            target = Integer.parseInt(inscan.next());
        } catch (Exception e) {
            error(input);
            return false;
        }
        while (inscan.hasNext()) {
            try {
                numbers.add(Integer.parseInt(inscan.next()));
            } catch (Exception e) {
                error(input);
                return false;
            }
        }
        return true;

    }

    /**
     * Takes a string of operations in +*+*+ format and converts it into the desired
     * format with the numbers.
     *
     * @param operations String of operations to parse.
     * @param normal     normal if true, left to right if false.
     */
    public static void outputFormat(String operations, boolean normal) {
        if (operations.contains("X")) {
            impossible(input);
            return;
        }
        String output = "";
        if (normal) {
            output += "N ";
        } else {
            output += "L ";
        }
        output += "" + target + " = " + first + " ";
        for (int i = 0; i < numbers.size(); i++) {
            if (i < operations.length()) {
                output += operations.charAt(i) + " ";
            }
            output += numbers.get(i) + " ";

        }
        System.out.println(output);
    }

    /**
     * Prints an error message to stdout if a bad line is detected.
     *
     * @param line String, the erroneous input line.
     */
    public static void error(String line) {
        System.out.println(line + " Invalid");
    }

    public static void impossible(String line) {
        System.out.println(line + " impossible");
    }
}
