#include <iostream>
#include <unordered_map>
#include <tuple>
#include <iterator>
#include <fstream>
#include <sstream>
#include <vector>
using namespace std;

class Assembler
{
private:
    unordered_map<string, tuple<string, string>> opcode;
    unordered_map<string, tuple<int, int>> symtab, lttab;
    unordered_map<string, string> regncond;
    ifstream sourceCode;
    ofstream intermediateCode;
    int LC = 0;

    void create_optab()
    {
        addIS();
        addAD();
        addDS();
        addCodes();
    }

    void addIS()
    {
        for (int i = 0; i < 11; i++)
        {
            string instName[] = {"STOP", "ADD", "SUB", "MULT", "MOVER", "MOVEM", "COMP", "BC", "DIV", "READ", "PRINT"};
            string mnemomic = (i < 10 ? "0" : "") + to_string(i);
            tuple<string, string> temp = make_tuple("IS", mnemomic);
            opcode[instName[i]] = temp;
        }
    }

    void addAD()
    {
        for (int i = 0; i < 5; i++)
        {
            string instName[] = {"START", "END", "ORIGIN", "EQU", "LTORG"};
            string mnemomic = (i < 10 ? "0" : "") + to_string(i + 1);
            tuple<string, string> temp = make_tuple("AD", mnemomic);
            opcode[instName[i]] = temp;
        }
    }

    void addDS()
    {
        tuple<string, string> op1 = make_tuple("DL", "01");
        tuple<string, string> op2 = make_tuple("DL", "02");
        opcode["DC"] = op1;
        opcode["DS"] = op2;
    }

    void addCodes()
    {
        regncond["AREG"] = "1";
        regncond["BREG"] = "2";
        regncond["CREG"] = "3";
        regncond["DREG"] = "4";
        regncond["LT"] = "1";
        regncond["LE"] = "2";
        regncond["EQ"] = "3";
        regncond["GT"] = "4";
        regncond["GE"] = "5";
        regncond["ANY"] = "6";
    }

    string getType(const string& inst)
    {
        return get<0>(opcode.at(inst));
    }

    string convertConst(const string& value)
    {
        return "(C, " + value + ")";
    }

    string getIntermediate(const string& inst)
    {
        return "(" + get<0>(opcode.at(inst)) + ", " + get<1>(opcode.at(inst)) + ")";
    }

    string convertReg(const string& oper)
    {
        return "(" + regncond.at(oper) + ")";
    }

    string convertSymLit(const string& symlit)
    {
        if (symlit.empty())
        {
            return "";
        }
        else if (symlit[0] == '=')
        {
            int ordenal = get<1>(lttab.at(symlit));
            return "(L, " + to_string(ordenal) + ")";
        }
        else
        {
            int ordenal = get<1>(symtab.at(symlit));
            return "(S, " + to_string(ordenal) + ")";
        }
    }

    void addSymLit(string symlit) {
        if (symlit[0] == '=') {
            if (lttab.find(symlit) == lttab.end()) {
                lttab[symlit] = make_tuple(-1, lttab.size());
            }
        } else {
            if (symtab.find(symlit) == symtab.end()) {
                symtab[symlit] = make_tuple(-1, symtab.size());
            }
        }
    }

    void alloc_mem(string symlit)
    {
        if (symlit == "L")
        {
            int totEle = lttab.bucket_count();
            cout<<totEle<<endl;
            for (int i = 0; i < totEle; i++)
            {
                for (auto itr = lttab.begin(i); itr != lttab.end(i); itr++)
                {
                    if (get<0>(itr->second) == -1)
                    {
                        get<0>(itr->second) = LC;
                        LC++;
                        // write the DL statement to file
                        // cout << "\t" << "(DL, 01)" << "\t" << convertConst(itr->first.substr(2, itr->first.length() - 3)) << endl;
                        intermediateCode << "\t" << "(DL, 01)" << "\t" << convertConst(itr->first.substr(2, itr->first.length() - 3)) << endl;
                    }
                }
            }
        }
        else
        {
            if (symtab.find(symlit) != symtab.end())
            {
                get<0>(symtab[symlit]) = LC;
                LC++;
            }
        }
    }

    int getAddrSym(const string& sym) {
        size_t index = sym.find('+');
        string symNew = (index != string::npos) ? sym.substr(0, index) : sym;
        if (symtab.find(symNew) != symtab.end()) {
            intermediateCode << "\t" << "(AD, 03)" << "\t" << convertSymLit(symNew) << " + " << convertConst(sym.substr(index + 1)) << endl;
            return get<0>(symtab[symNew]);
        } else {
            return -1;
        }
    }

    void deserialize(const unordered_map<string, tuple<int, int>>& dataStr, const string& nameOfFile)
    {
        ofstream tableOut(nameOfFile);
        if (!tableOut.is_open()) {
            cerr << "Error opening file for writing: " << nameOfFile << endl;
            return;
        }
        for (const auto& [key, value] : dataStr)
        {
            tableOut << get<1>(value) << " " << key << " " << get<0>(value) << endl;
        }
        tableOut.close();
    }

public:
    Assembler()
    {
        create_optab();
    }

    void passI()
    {
        sourceCode.open("./source.asm");
        if (!sourceCode.is_open()) {
            cerr << "Error opening source file." << endl;
            return;
        }

        intermediateCode.open("./interCode.txt");
        if (!intermediateCode.is_open()) {
            cerr << "Error opening intermediate code file." << endl;
            return;
        }

        string line;
        while (getline(sourceCode, line))
        {
            stringstream ss(line);
            vector<string> code;
            string word;
            while (ss >> word)
            {
                code.push_back(word);
            }
            int cSize = code.size();

            for (int i = 0; i < cSize; i++)
            {
                if (opcode.find(code[i]) != opcode.end())
                {
                    string type = getType(code[i]);

                    // for AD inst
                    if (type == "AD")
                    {
                        if (code[i] == "START")
                        {
                            if (i + 1 < cSize)
                            {
                                intermediateCode << "\t" << "\t" << getIntermediate(code[i]) << "\t" << convertConst(code[i + 1]) << endl;
                                LC = stoi(code[i + 1]);
                            }
                            else
                            {
                                intermediateCode << "\t" << "\t" << getIntermediate(code[i]) << endl;
                                LC = 0;
                            }
                        }
                        else if (code[i] == "END")
                        {
                            intermediateCode << "\t" << "\t" << getIntermediate(code[i]) << endl;
                            alloc_mem("L");
                            return;
                        }
                        else if (code[i] == "LTORG")
                        {
                            alloc_mem("L");
                            intermediateCode << "\t" << "\t" << getIntermediate(code[i]) << endl;
                        }
                        if (code[i] == "ORIGIN")
                        {
                            if (i + 1 <= cSize)
                            {
                                // check for entry in symtab if a symbol else carry out like number
                                // cout << getIntermediate(code[i]) << convertConst(code[i + 1]) << endl;
                                intermediateCode << "\t" << getIntermediate(code[i]) << "\t" << convertConst(code[i + 1]) << endl;
                                // LC =
                                int newLC = getAddrSym(code[i + 1]);
                                if (newLC != -1)
                                {
                                    LC = newLC;
                                }
                                else if (newLC = -1)
                                {
                                    cout << "Symbol not found" << endl;
                                    // return;
                                }
                            }
                            else if (i + 1 > cSize)
                            {
                                cout << "Error assembling. Please check syntax" << endl;
                                // remove("interCode.txt");
                                return;
                            }
                        }
                        else if (code[i] == "EQU")
                        {
                            if (i > 0 && i + 1 < cSize)
                            {
                                string label = code[i - 1];
                                addSymLit(label);
                                get<0>(symtab[label]) = getAddrSym(code[i + 1]);
                                intermediateCode << convertSymLit(label) << "\t" << getIntermediate(code[i]) << "\t" << convertSymLit(code[i + 1]) << endl;
                            }
                            else
                            {
                                cerr << "Check syntax" << endl;
                                return;
                            }
                        }
                    }

                    // for DL inst
                    if (type == "DL")
                    {
                        if (i + 1 < cSize && i == 1)
                        {
                            if (code[i] == "DS")
                            {
                                alloc_mem(code[i - 1]);
                                intermediateCode << convertSymLit(code[i - 1]) << "\t" << getIntermediate(code[i]) << "\t" << convertConst(code[i + 1]) << endl;
                                LC += stoi(code[i + 1]);
                            }
                            else if (code[i] == "DC")
                            {
                                alloc_mem(code[i - 1]);
                                intermediateCode << convertSymLit(code[i - 1]) << "\t" << getIntermediate(code[i]) << "\t" << convertConst(code[i + 1]) << endl;
                                LC += 1;
                            }
                        }
                    }

                    // for IS Inst
                    if (type == "IS")
                    {
                        if (i + 2 < cSize)
                        {
                            string label = code[i - 1];
                            string reg_oper = code[i + 1];
                            string mem_oper = code[i + 2];
                            addSymLit(code[i - 1]);
                            addSymLit(code[i + 2]);
                            intermediateCode << convertSymLit(label) << "\t" << getIntermediate(code[i]) << "\t" << convertReg(reg_oper) << ", " << convertSymLit(mem_oper) << endl;
                            LC++;
                            if(label != "")
                            {
                                alloc_mem(label);
                            }
                        }
                        else if (i + 1 < cSize)
                        {
                            string label = code[i - 1];
                            intermediateCode << convertSymLit(label) << "\t" << getIntermediate(code[i]) << endl;
                        }
                    }
                }
            }
        }

        sourceCode.close();
        intermediateCode.close();
    }

    void generate_Table_Bin()
    {
        deserialize(lttab, "LTTAB.txt");
        deserialize(symtab, "SYMTAB.txt");
    }
};

int main()
{
    Assembler a1;
    a1.passI();
    a1.generate_Table_Bin();
    return 0;
}
