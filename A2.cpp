#include <iostream>
#include <unordered_map>
#include <tuple>
#include <fstream>
#include <sstream>
#include <vector>

using namespace std;

class Assembler {
private:
    unordered_map<int, tuple<string, int>> symtab, lttab;
    ifstream intermediateCodeFile;
    ofstream machineCodeFile;

    // Helper function to split strings by spaces
    vector<string> split(const string& s, char delimiter) {
        vector<string> tokens;
        istringstream tokenStream(s);
        string token;
        while (getline(tokenStream, token, delimiter)) {
            tokens.push_back(token);
        }
        return tokens;
    }

    // Helper function to get address from symbol or literal table
    string getAddress(const string& type, int index) {
        if (type == "S") {
            return to_string(get<1>(symtab.find(to_string(index))->second));
        } else if (type == "L") {
            return to_string(get<1>(lttab.find(to_string(index))->second));
        }
        return "";
    }

    void serialize(string tabName, unordered_map<int, tuple<string, int>> table)
    {
        ifstream tableIn;
        tableIn.open(tabName, ios::in);
        string line;
        while (getline(tableIn, line))
        {
            vector<string> tokens = split(line, ' ');
            for(int i=0;i<tokens.size();i++)
            {
                cout<<tokens[i]<<" ";
            }

        }
        tableIn.close();
        
    }

public:
    Assembler() {
        serialize("SYMTAB.txt")
    }


    void passII() {
        intermediateCodeFile.open("./interCode.txt");
        if (!intermediateCodeFile.is_open()) {
            cerr << "Error opening intermediate code file." << endl;
            return;
        }

        machineCodeFile.open("./machineCode.txt");
        if (!machineCodeFile.is_open()) {
            cerr << "Error opening machine code file." << endl;
            return;
        }

        string line;
        while (getline(intermediateCodeFile, line)) {
            vector<string> tokens = split(line, '\t');
            string finalMC = "+";
            if (tokens.size() == 3) {
                string address = tokens[0];
                string instruction = tokens[1];
                string operand = tokens[2];

                if (instruction.find("IS") != string::npos) {
                    finalMC += instruction.substr(instruction.length()-3, instruction.length());
                    vector<string> operList = split(operand, ', ');
                    string oper1 = operList[0];
                    string oper2T = operList[1];
                    string oper2O = operList[2];
                    finalMC += oper1.substr(1, 2);
                    int ordpos = stoi(oper2O.substr(0, 1));
                    string type = oper2T.substr(1, 2);
                    finalMC += getAddress(type, ordpos);
                    cout<<finalMC<<endl;

                } else if (instruction.find("DL") != string::npos) {
                    machineCodeFile << address << " " << instruction << " " << operand << endl;
                }
            }
        }

        intermediateCodeFile.close();
        machineCodeFile.close();
    }
};

int main() {
    Assembler assembler;

    // Load symbol and literal tables
    assembler.loadSymbolTable("SYMTAB.txt");
    assembler.loadLiteralTable("LTTAB.txt");

    // Run Pass II
    assembler.passII();

    return 0;
}
