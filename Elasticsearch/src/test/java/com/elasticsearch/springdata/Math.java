package com.elasticsearch.springdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author: Jacknolfskin
 * @Date: 2018/5/4 17:25
 * @Path: com.springdata
 */
public class Math {
    /**
     * 零
     */
    public static final int ZERO = 0;
    /**
     * 加法
     */
    public static final Integer ADD = 0;
    /**
     * 减法
     */
    public static final Integer SUBTRACT = 1;
    /**
     * 乘法
     */
    public static final Integer MULTIPY = 2;
    /**
     * 除法
     */
    public static final Integer DIVIDE = 3;

    /**
     * 比较
     */
    public static final Integer COMPARE = 4;

    private static List<Question> list = new ArrayList<>();

    public static void main(String[] args) {
        //System.out.println(generate41());
        //System.out.println(generate42());
        //System.out.println(generate43(10000));
        //System.out.println(generate45());
        //System.out.println(generate46());
        //System.out.println(generate50());
        //System.out.println(generate51());
        //System.out.println(generate52());
        //System.out.println(generate54());
        //System.out.println(generate55());
        //System.out.println(generate56());
        //System.out.println(generate59());
        //System.out.println(generate60());
        //System.out.println(generate65());
        //System.out.println(generate72());
        //System.out.println(generate74());
        //System.out.println(generate75());
        System.out.println(generate76());
    }

    /**
     * 41
     *
     * @return
     */
    private static List<Question> generate41() {
        Random r = new Random();
        int a = 0, b = 0;
        Question question = new Question();
        for (int i = 1; i <= 100; i++) {
            do {
                a = r.nextInt(1000);
                b = r.nextInt(1000);
                question.setFrontNum(a);
                question.setBackNum(b);
            } while (a - b < 100);
            question.setResult((a - b));
            question.setSign(SUBTRACT);
            list.add(question);
        }
        return list;
    }

    /**
     * 42
     *
     * @return
     */
    private static List<Question> generate42() {
        Random r = new Random();
        int a, b;
        for (int i = 1; i <= 100; i++) {
            Question question = new Question();
            do {
                a = r.nextInt(1000);
                b = r.nextInt(1000);
            } while ((a % 10 > b % 10) || (a % 100 > b % 100) || a - b <= 0);
            question.setFrontNum(a);
            question.setBackNum(b);
            question.setSign(SUBTRACT);
            question.setResult(a - b);
            list.add(question);
        }
        return list;
    }

    /**
     * 43
     *
     * @return
     */
    private static List<Question> generate43(int num) {
        Random r = new Random();
        int a, b, c;
        for (int i = 1; i <= 100; i++) {
            Question question = new Question();
            c = r.nextInt(2);
            do {
                a = r.nextInt(num);
                b = r.nextInt(num);
                question.setFrontNum(a);
                question.setBackNum(b);
            } while ((c == 1 && b == 0)
                    || (c == 1 && a < b)
                    || (c == 0 && a + b > 10000));
            switch (c) {
                case 0:
                    question.setSign(ADD);
                    question.setResult(a + b);
                    break;
                case 1:
                    question.setSign(SUBTRACT);
                    question.setResult(a - b);
                    break;
                default:
                    break;
            }
            list.add(question);
        }
        return list;
    }

    /**
     * 45
     *
     * @return
     */
    private static List<Question> generate45() {
        Random r = new Random();
        int a, b;
        for (int i = 1; i <= 100; i++) {
            Question question = new Question();
            do {
                a = r.nextInt(10000);
                b = r.nextInt(10000);
                question.setFrontNum(a);
                question.setBackNum(b);
            } while ((a % 100 != 0) && (b % 100 != 0) && (a + b > 10000));
            question.setSign(ADD);
            question.setResult(a + b);
            list.add(question);
        }
        return list;
    }

    /**
     * 46
     *
     * @return
     */
    private static List<Question> generate46() {
        Random r = new Random();
        int a, b;
        for (int i = 1; i <= 100; i++) {
            Question question = new Question();
            do {
                a = r.nextInt(10000);
                b = r.nextInt(10000);
            } while ((a % 100 != 0) && (b % 100 != 0) && (a - b < 0));
            question.setFrontNum(a);
            question.setBackNum(b);
            question.setSign(SUBTRACT);
            question.setResult(a - b);
            list.add(question);
        }
        return list;
    }

    /**
     * 49
     *
     * @return
     */
    private static List<Question> generate49() {
        Random rand = new Random();
        Question question = new Question();
        for (int a = 0; a < 100; a++) {
            int add1 = rand.nextInt(9997) + 1;
            int[] datas = {add1 / 1000, add1 % 1000 / 100, add1 % 1000 % 100 / 10, add1 % 1000 % 100 % 10};
            for (int i = 0; i < 4; i++) {
                if (datas[i] == 0) {
                    datas[i] = -1;
                } else {
                    break;
                }
            }
            int add2 = 0;
            for (int i = 0; i < 4; i++) {
                if (datas[i] != -1) {
                    add2 += (rand.nextInt(10 - datas[i])) * getDigit(3 - i);
                }
            }
            //System.out.println(add1 + "+" + add2 + "=" + (add1 + add2));
            question.setFrontNum(add1);
            question.setBackNum(add2);
            question.setResult((add1 + add2));
            question.setSign(ADD);
            list.add(question);
        }

        return list;
    }

    private static int getDigit(int digit) {
        String test = "1";
        for (int i = 0; i < digit; i++) {
            test += "0";
        }
        return Integer.parseInt(test);
    }

    /**
     * 50
     *
     * @return
     */
    private static List<Question> generate50() {
        Random r = new Random();
        int a, b;
        for (int i = 1; i <= 100; i++) {
            Question question = new Question();
            do {
                a = r.nextInt(10000);
                b = r.nextInt(10000);
            } while (((a % 10 + b % 10) < 10 || (a % 100 + b % 100) < 10 || a + b > 10000));
            question.setFrontNum(a);
            question.setBackNum(b);
            question.setSign(ADD);
            question.setResult(a + b);
            list.add(question);
        }
        return list;
    }

    /**
     * 51
     *
     * @return
     */
    private static List<Question> generate51() {
        Random r = new Random();
        int a, b;
        for (int i = 1; i <= 100; i++) {
            Question question = new Question();
            do {
                a = r.nextInt(10000);
                b = r.nextInt(10000);
            } while (((a % 10 - b % 10) < 0 || (a % 100 - b % 100) < 0 || a - b < 0));
            question.setFrontNum(a);
            question.setBackNum(b);
            question.setSign(SUBTRACT);
            question.setResult(a - b);
            list.add(question);
        }
        return list;
    }

    /**
     * 52
     * @return
     */
    private static List<Question> generate52() {
        Random r = new Random();
        int a, b;
        for (int i = 1; i <= 100; i++) {
            Question question = new Question();
            do {
                a = r.nextInt(10000);
                b = r.nextInt(10000);
            } while (((a % 10 - b % 10) >= 0 || (a % 100 - b % 100) >= 0 || a - b < 0));
            question.setFrontNum(a);
            question.setBackNum(b);
            question.setSign(SUBTRACT);
            question.setResult(a - b);
            list.add(question);
        }
        return list;
    }

    /**
     * 54
     *
     * @return
     */
    private static List<Question> generate54() {
        Random r = new Random();
        int a, b;
        for (int i = 1; i <= 100; i++) {
            Question question = new Question();
            do {
                a = r.nextInt(100);
                b = r.nextInt(9);
            } while ((a < 10 && b == 0));
            question.setFrontNum(a);
            question.setBackNum(b);
            question.setSign(SUBTRACT);
            question.setResult(a * b);
            list.add(question);
        }
        return list;
    }

    /**
     * 55
     *
     * @return
     */
    private static List<Question> generate55() {
        Random r = new Random();
        int a, b;
        for (int i = 1; i <= 100; i++) {
            Question question = new Question();
            do {
                a = r.nextInt(1000);
                b = r.nextInt(9);
            } while (a > 100 && b == 0);
            question.setFrontNum(a);
            question.setBackNum(b);
            question.setSign(SUBTRACT);
            question.setResult(a * b);
            list.add(question);
        }
        return list;
    }

    /**
     * 56
     *
     * @return
     */
    private static List<Question> generate56() {
        Random r = new Random();
        int a = 0, b = 10;
        for (int i = 1; i <= 100; i++) {
            Question question = new Question();
            do {
                a = r.nextInt(1000);
            } while (a == 0);
            question.setFrontNum(a);
            question.setBackNum(10);
            question.setSign(SUBTRACT);
            question.setResult(a * b);
            list.add(question);
        }
        return list;
    }

    /**
     * 59
     *
     * @return
     */
    private static List<Question> generate59() {
        Random r = new Random();
        int a = 0, b = 10;
        for (int i = 1; i <= 100; i++) {
            Question question = new Question();
            do {
                a = r.nextInt(1000);
            } while (a % 10 != 0);
            question.setFrontNum(a);
            question.setBackNum(10);
            question.setSign(DIVIDE);
            question.setResult(a / b);
            list.add(question);
        }
        return list;
    }

    /**
     * 60
     *
     * @return
     */
    private static List<Question> generate60() {
        Random r = new Random();
        int a = 0, b = 0;
        for (int i = 1; i <= 100; i++) {
            Question question = new Question();
            do {
                a = r.nextInt(100);
                b = r.nextInt(100);
                question.setFrontNum(a);
                question.setBackNum(b);
            } while (a < 10 || b % 10 != 0);
            question.setSign(MULTIPY);
            question.setResult(a * b);
            list.add(question);
        }
        return list;
    }

    /**
     * 65
     *
     * @return
     */
    private static List<Question> generate65() {
        Random r = new Random();
        int a = 0, b = 0, c = 0, d = 0;
        for (int i = 1; i <= 100; i++) {
            Question question = new Question();
            do {
                a = r.nextInt(5);
                b = r.nextInt(5);
                c = r.nextInt(5);
                d = r.nextInt(5);
            } while (a == 0 || a == 1 || b == 0 || b == 1 || c == 0 || c == 1 || d == 0 || d == 1);
            question.setFrontNum(a);
            question.setFrontDenominatorNum(b);
            question.setBackNum(c);
            question.setBackDenominatorNum(d);
            question.setSign(COMPARE);
            list.add(question);
        }
        return list;
    }

    /**
     * 72
     *
     * @return
     */
    private static List<Question> generate72() {
        Random r = new Random();
        int a = 0, b = 0;
        double c = 0.01;
        for (int i = 1; i <= 100; i++) {
            Question question = new Question();
            do {
                a = r.nextInt(5);
                b = r.nextInt(5);
            } while (a == 0 || b == 0);
            question.setFrontDecimalNum((float) (a * c));
            question.setBackDecimalNum((float) (b * c));
            question.setSign(COMPARE);
            list.add(question);
        }
        return list;
    }

    /**
     * 74
     *
     * @return
     */
    private static List<Question> generate74() {
        Random r = new Random();
        int a = 0, b = 0, d = 0, e = 0;
        double c = 0.0, f = 0.0;
        for (int i = 1; i <= 100; i++) {
            Question question = new Question();
            do {
                a = r.nextInt(10);
                b = r.nextInt(10);
                d = r.nextInt(4);
                e = r.nextInt(4);
            } while (a == 0 || b == 0 || d == 0 || e == 0);
            switch (d) {
                case 1:
                    c = 0.1;
                    break;
                case 2:
                    c = 0.01;
                    break;
                case 3:
                    c = 0.001;
                    break;
                case 4:
                    c = 0.0001;
                    break;
            }
            switch (e) {
                case 1:
                    f = 0.1;
                    break;
                case 2:
                    f = 0.01;
                    break;
                case 3:
                    f = 0.001;
                    break;
                case 4:
                    f = 0.0001;
                    break;
            }
            question.setFrontDecimalNum((float) (a * c));
            question.setBackDecimalNum((float) (b * f));
            question.setSign(COMPARE);
            list.add(question);
        }
        return list;
    }

    /**
     * 75
     *
     * @return
     */
    private static List<Question> generate75() {
        Random r = new Random();
        int a = 0, b = 0;
        double c = 0.1;
        for (int i = 1; i <= 100; i++) {
            Question question = new Question();
            do {
                a = r.nextInt(9);
                b = r.nextInt(9);
            } while (a == 0 || b == 0);
            question.setFrontDecimalNum((float) (a * c));
            question.setBackDecimalNum((float) (b * c));
            question.setDecimalResult((float) (a * c + b * c));
            question.setSign(ADD);
            list.add(question);
        }
        return list;
    }

    /**
     * 76
     *
     * @return
     */
    private static List<Question> generate76() {
        Random r = new Random();
        int a = 0, b = 0, d = 0, e = 0;
        double c = 0.0, f = 0.0;
        for (int i = 1; i <= 100; i++) {
            Question question = new Question();
            do {
                a = r.nextInt(9);
                b = r.nextInt(9);
                d = r.nextInt(4);
                e = r.nextInt(4);
            } while (a == 0 || b == 0 || d == 0 || e == 0);
            switch (d) {
                case 1:
                    c = 0.1;
                    break;
                case 2:
                    c = 0.01;
                    break;
                case 3:
                    c = 0.001;
                    break;
                case 4:
                    c = 0.0001;
                    break;
            }
            switch (e) {
                case 1:
                    f = 0.1;
                    break;
                case 2:
                    f = 0.01;
                    break;
                case 3:
                    f = 0.001;
                    break;
                case 4:
                    f = 0.0001;
                    break;
            }
            question.setFrontDecimalNum((float) (a * c));
            question.setBackDecimalNum((float) (b * f));
            question.setDecimalResult((float) (a * c + b * f));
            question.setSign(ADD);
            list.add(question);
        }
        return list;
    }

}
