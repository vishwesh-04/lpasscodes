package Mock_I.Assignment_A1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

public class RI {
    private String input;
    private Map<String, String> mp;
    private Map<String, String> regMap;
    private Map<String, Integer> symbol;
    private Map<String, Integer> literal;
    private int error;
    private int lc;

    public RI(String input) {
        this.input = input;
        mp = new HashMap<>();
        regMap = new HashMap<>();
        symbol = new HashMap<>();
        literal = new HashMap<>();

        mp.put("stop", "(IS,00)");
        mp.put("add", "(IS,01)");
        mp.put("sub", "(IS,02)");
        mp.put("mult", "(IS,03)");
        mp.put("mover", "(IS,04)");
        mp.put("movem", "(IS,05)");
        mp.put("comp", "(IS,06)");
        mp.put("bc", "(IS,07)");
        mp.put("div", "(IS,08)");
        mp.put("read", "(IS,09)");
        mp.put("print", "(IS,10)");
        mp.put("start", "(AD,01)");
        mp.put("end", "(AD,02)");
        mp.put("origin", "(AD,03)");
        mp.put("equ", "(AD,04)");
        mp.put("ltorg", "(AD,05)");
        mp.put("dc", "(DL,01)");
        mp.put("ds", "(DL,02)");

        regMap.put("areg", "1");
        regMap.put("breg", "2");
        regMap.put("creg", "3");
        regMap.put("dreg", "4");
    }

    private int present(String l, Map<String, Integer> a) {
        if (a.containsKey(l)) {
            return a.get(l);
        }
        return 0;
    }

    public void tokenize() throws IOException {
        int symCount = 0;
        int literalCount = 0;
        String line1;
        BufferedReader inputFile = new BufferedReader(new FileReader("input_file1.txt"));
        BufferedWriter literalFile = new BufferedWriter(new FileWriter("literal_file.txt"));
        BufferedWriter literalFile1 = new BufferedWriter(new FileWriter("literal_file1.txt"));
        BufferedWriter symbolFile = new BufferedWriter(new FileWriter("symbol_file.txt"));

        if (!inputFile.ready()) {
            System.out.println("Error in opening input file");
        }
        if (!literalFile.ready()) {
            System.out.println("Error in opening literal file");
        }
        if (!literalFile1.ready()) {
            System.out.println("Error in opening literal file1");
        }

        while ((line1 = inputFile.readLine()) != null) {
            StringTokenizer tokenizer = new StringTokenizer(line1, " ");
            Vector<String> tokens = new Vector<>();
            while (tokenizer.hasMoreTokens()) {
                tokens.add(tokenizer.nextToken());
            }

            Map<String, String>.Entry<String, String> entry = mp.entrySet().iterator().next();
            int flag = 0;
            int flag1 = 0;
            for (Map.Entry<String, String> itr : mp.entrySet()) {
                if (itr.getKey().equals(tokens.get(0)) || itr.getKey().equals(tokens.get(1))) {
                    flag = 1;
                    if (itr.getKey().equals(tokens.get(1))) {
                        flag1 = 1;
                    }
                    break;
                }
            }
            if (flag != 0 && flag1 == 0) {
                if (tokens.get(0).equals("start") && tokens.size() > 1) {
                    lc = Integer.parseInt(tokens.get(1));
                    // lc--;
                    System.out.println(mp.get(tokens.get(0)) + "(C," + tokens.get(1) + ")");
                    continue; // Skip further processing for this line
                } else if (tokens.get(0).equals("dc")) {
                    int temp;
                    System.out.println(lc + " " + mp.get(tokens.get(0)) + "(C," + tokens.get(1) + ")");
                    if (!present(tokens.get(1), symbol)) {
                        symbol.put(tokens.get(1), ++symCount);
                        symbolFile.write(symCount + " " + tokens.get(1) + " " + lc + "\n");
                    }
                } else if (tokens.get(0).equals("ds")) {
                    System.out.println(lc + " " + mp.get(tokens.get(0)) + "(C," + tokens.get(1) + ")");
                    lc += Integer.parseInt(tokens.get(1));
                    lc--;
                } else if (tokens.get(0).equals("end") || tokens.get(0).equals("ltorg")) {
                    if (tokens.get(0 ).equals("end")) {
                        System.out.print(lc + " " + mp.get(tokens.get(0)));
                        break;
                    }
                    String io;
                    Vector<String> lines = new Vector<>();

                    literalFile.seek(0);
                    while ((io = literalFile.readLine()) != null) {
                        io += " " + lc;
                        lc++;
                        lines.add(io);
                    }
                    literalFile1.seek(0);
                    for (int k = 0; k < lines.size(); k++) {
                        literalFile1.write(lines.get(k) + "\n");
                    }
                } else {
                    System.out.print(lc + " " + mp.get(tokens.get(0)));
                }

                for (int i = 1; i < tokens.size(); i++) {
                    if (regMap.containsKey(tokens.get(i))) {
                        System.out.print("(" + regMap.get(tokens.get(i)) + ") ");
                    } else if (Character.isDigit(tokens.get(i).charAt(0))) {
                        int temp;
                        if (present(tokens.get(i), literal)) {
                            temp = present(tokens.get(i), literal);
                        } else {
                            literalCount++;
                            temp = literalCount;
                            literal.put(tokens.get(i), literalCount);
                            // literalFile.write(literalCount + " " + tokens.get(i) + "\n");
                        }
                        System.out.print("(L," + temp + ") ");
                    } else {
                        int temp;
                        if (present(tokens.get(i), symbol)) {
                            temp = present(tokens.get(i), symbol);
                        } else {
                            symCount++;
                            temp = symCount;
                            symbol.put(tokens.get(i), symCount);
                            // symbolFile.write(symCount + " " + tokens.get(i) + "\n");
                        }
                        System.out.print("(S," + temp + ") ");
                    }
                }
                System.out.println();
            } else if (flag1 == 1) {
                int temp;
                if (present(tokens.get(0), symbol)) {
                    temp = present(tokens.get(0), symbol);
                    symbol.put(tokens.get(0), temp);
                    symbolFile.write(temp + " " + tokens.get(0) + " " + lc + "\n");
                } else {
                    symbol.put(tokens.get(0), ++symCount);
                    symbolFile.write(symCount + " " + tokens.get(0) + " " + lc + "\n");
                }
                System.out.print(lc + " " + mp.get(tokens.get(1)));

                for (int i = 2; i < tokens.size(); i++) {
                    if (regMap.containsKey(tokens.get(i))) {
                        System.out.print("(" + regMap.get(tokens.get(i)) + ") ");
                    } else if (Character.isDigit(tokens.get(i).charAt(0))) {
                        int temp;
                        if (present(tokens.get(i), literal)) {
                            temp = present(tokens.get(i), literal);
                        } else {
                            literalCount++;
                            temp = literalCount;
                            literal.put(tokens.get(i), literalCount);
                            // literalFile.write(literalCount + " " + tokens.get(i) + "\n");
                        }
                        System.out.print("(L," + temp + ") ");
                    } else {
                        int temp;
                        if (present(tokens.get(i), symbol)) {
                            temp = present(tokens.get(i), symbol);
                        } else {
                            symCount++;
                            temp = symCount;
                            symbol.put(tokens.get(i), symCount);
                            // symbolFile.write(symCount + " " + tokens.get(i) + "\n");
                        }
                        System.out.print("(S," + temp + ") ");
                    }
                }
                System.out.println();
            } else {
                System.out.println("Invalid Error:");
                error = 1;
                break;
            }
            lc++;
        }
        literalFile.close();
        inputFile.close();
        symbolFile.close();
    }

    public static void main(String[] args) throws IOException {
        String input;
        System.out.println("Enter the string: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        input = reader.readLine();
        RI obj1 = new RI(input);
        obj1.tokenize();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("FINISH");
    }
}