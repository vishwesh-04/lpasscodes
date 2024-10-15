package Mock_I.Assignment_A1;

import java.io.*;
import java.util.*;

class Mnenomics {
    String name, type;
    int code;

    Mnenomics(String name, String type, int code) {
        this.name = name;
        this.type = type;
        this.code = code;
    }
}

class TableEntry {
    int ordenal;
    int addr;
    String name;

    TableEntry(int ordenal, String name, int addr) {
        this.ordenal = ordenal;
        this.name = name;
        this.addr = addr;
    }
}

public class PassI {
    File fin = new File("Mock_I/Assignment_A1/source.asm");
    File fout = new File("Mock_I/Assignment_A1/interCode.txt");
    ArrayList<Mnenomics> opcodeTab = new ArrayList<>();
    ArrayList<TableEntry> symTab = new ArrayList<>();
    ArrayList<TableEntry> litTab = new ArrayList<>();
    Map<String, Integer> registerNcond = new HashMap<>();
    int LC = 0;

    void create_req_tables() {

        // AD instructions
        opcodeTab.add(new Mnenomics("START", "AD", 01));
        opcodeTab.add(new Mnenomics("EQU", "AD", 04));
        opcodeTab.add(new Mnenomics("LTORG", "AD", 05));
        opcodeTab.add(new Mnenomics("ORIGIN", "AD", 03));
        opcodeTab.add(new Mnenomics("END", "AD", 02));

        // IS instructions
        opcodeTab.add(new Mnenomics("STOP", "IS", 00));
        opcodeTab.add(new Mnenomics("ADD", "IS", 01));
        opcodeTab.add(new Mnenomics("SUB", "IS", 02));
        opcodeTab.add(new Mnenomics("MULT", "IS", 03));
        opcodeTab.add(new Mnenomics("MOVER", "IS", 04));
        opcodeTab.add(new Mnenomics("MOVEM", "IS", 05));
        opcodeTab.add(new Mnenomics("COMP", "IS", 06));
        opcodeTab.add(new Mnenomics("BC", "IS", 07));
        opcodeTab.add(new Mnenomics("DIV", "IS", 8));
        opcodeTab.add(new Mnenomics("READ", "IS", 9));
        opcodeTab.add(new Mnenomics("PRINT", "IS", 10));

        // DL instrcution
        opcodeTab.add(new Mnenomics("Dc", "DL", 01));
        opcodeTab.add(new Mnenomics("DS", "DL", 02));

        // add req register and conditions
        registerNcond.put("AREG", 1);
        registerNcond.put("BREG", 2);
        registerNcond.put("CREG", 3);
        registerNcond.put("DREG", 4);
        registerNcond.put("LT", 1);
        registerNcond.put("LE", 2);
        registerNcond.put("EQ", 3);
        registerNcond.put("GT", 4);
        registerNcond.put("GE", 5);
        registerNcond.put("ANY", 6);

    }

    void assembler_dir(String inst, String param) {
        if (inst.equals("START")) {
            LC = Integer.parseInt(param);
        }
        if (inst.equals("END")) {
            this.alloc_mem("literal");
        }
        if (inst.equals("LTORG")) {
            this.alloc_mem("literal");
        }
        if (inst.equals("ORIGIN")) {
            LC = this.getAddress(param);
        }
        if (inst.equals("EQU")) 
        {
            
        }
    }

    void declarative_st(String label, String inst, String param1) {
        if (inst.equals("DC")) {
            for (TableEntry tableEntry : symTab) {
                if (tableEntry.name.equals(label))
                    tableEntry.addr = LC;
            }
            LC += 1;
        }
        if (inst.equals("DS")) {
            for (TableEntry tableEntry : symTab) {
                if (tableEntry.name.equals(label))
                    tableEntry.addr = LC;
            }
            LC += Integer.parseInt(param1);
        }
    }

    String generateIC(String label, String inst, String param1, String param2) {
        // for label
        String equIC = "";
        for (TableEntry tableEntry : symTab) {
            if (tableEntry.name.equals(label))
                equIC += "(S," + tableEntry.ordenal + ")";
        }
        equIC += "\t";

        // for inst
        for (Mnenomics mnenomics : opcodeTab) {
            if (mnenomics.name.equals(inst))
                equIC += "\t(" + mnenomics.type + ", " + mnenomics.code + ")";
                
        }

        // for param1 i.e. register

        if(param1.matches("[0-9]+"))
        {
            equIC += "(C," + Integer.parseInt(param1) + ")";
        }
        
        else
        {
            equIC += "(" + registerNcond.get(param1) + ")";
        }

        // for param2 i.e. memory
        
        for (TableEntry tableEntry : litTab) {
            if (tableEntry.name.equals(param2)) {
                equIC += "\t(L," + tableEntry.ordenal + ")";
            }
        }
        for (TableEntry tableEntry : symTab) {
            if (tableEntry.name.equals(param2)) {
                equIC += "\t(S," + tableEntry.ordenal + ")";
            }
        }
        
        return equIC;
    }

    private int getAddress(String param) {

        throw new UnsupportedOperationException("Unimplemented method 'getAddress'");
    }

    private void alloc_mem(String alloc_to) {
        throw new UnsupportedOperationException("Unimplemented method 'alloc_mem'");
    }

    void perform() {
        try (BufferedReader br = new BufferedReader(new FileReader(fin))) {
            String line;
            String instruction, label, operand1, operand2;
            // BufferedWriter bw = new BufferedWriter(new FileWriter(fout));
            String newCode = "";
            while ((line = br.readLine()) != null) {
                String[] arrLine = line.split(" ");
                if (arrLine.length == 2) {
                    instruction = arrLine[0];
                    operand1 = arrLine[1];
                    newCode = this.generateIC("", instruction, operand1, "");
                    
                    
                } else if (arrLine.length > 2) {
                    label = arrLine[0];
                    instruction = arrLine[1];
                    if (arrLine.length == 3) {
                        operand1 = arrLine[2];
                        newCode = this.generateIC(label, instruction, operand1, "");
                    } else {
                        operand1 = arrLine[2];
                        operand2 = arrLine[3];
                        newCode = this.generateIC(label, instruction, operand1, operand2);
                    }

                }
                LC+=1;
                System.out.println(newCode);

            }
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

}
