#include <iostream>
#include <unordered_map>
#include <tuple>
#include <fstream>
#include <sstream>
#include <vector>
#include <iomanip> // For std::setw and std::setfill

using namespace std;

class Assembler
{
private:
    unordered_map<int, tuple<string, int>> symtab; // Symbol table
    unordered_map<int, tuple<string, int>> lttab;  // Literal table
    ifstream intermediateCodeFile;
    ofstream machineCodeFile;

    // Helper function to split strings by spaces
    vector<string> split(const string &s, char delimiter)
    {
        vector<string> tokens;
        istringstream tokenStream(s);
        string token;
        while (getline(tokenStream, token, delimiter))
        {
            tokens.push_back(token);
        }
        return tokens;
    }

    // Helper function to get address from symbol or literal table
    string getAddress(string type, int index)
    {
        cout << type << " " << index << endl;
        if (type == "S")
        {
            unordered_map<int, tuple<string, int>>::iterator itr;
            itr = symtab.begin();
            while (itr != symtab.end())
            {
                if (itr->first == index and get<1>(itr->second) != -1)
                {
                    return to_string(get<1>(itr->second));
                }
                itr++;
            }
        }
        if (type == "L")
        {
            unordered_map<int, tuple<string, int>>::iterator itr;
            itr = lttab.begin();
            while (itr != lttab.end())
            {
                if (itr->first == index and get<1>(itr->second) != -1)
                {
                    return to_string(get<1>(itr->second));
                }
                itr++;
            }
        }
        return "";
    }

    void loadTable(const string &tabName, unordered_map<int, tuple<string, int>> &table)
    {
        ifstream tableIn(tabName);
        if (!tableIn.is_open())
        {
            cerr << "Error opening table file: " << tabName << endl;
            return;
        }
        string line;
        while (getline(tableIn, line))
        {
            vector<string> tokens = split(line, ' ');
            if (tokens.size() == 3)
            {
                int index = stoi(tokens[0]);
                string symbol = tokens[1];
                int address = stoi(tokens[2]);
                table[index] = make_tuple(symbol, address);
            }
        }
        tableIn.close();
    }

    std::string padWithLeadingZeros(const std::string &str, size_t length = 6)
    {
        std::ostringstream oss;
        oss << std::setw(length) << std::setfill('0') << str;
        return oss.str();
    }

public:
    Assembler()
    {
        // Constructor: Optionally initialize tables or other settings if needed
    }

    void loadSymbolTable(const string &filename)
    {
        loadTable(filename, symtab);
    }

    void loadLiteralTable(const string &filename)
    {
        loadTable(filename, lttab);
    }

    void passII()
    {
        intermediateCodeFile.open("./interCode.txt");
        if (!intermediateCodeFile.is_open())
        {
            cerr << "Error opening intermediate code file." << endl;
            return;
        }

        machineCodeFile.open("./machineCode.txt");
        if (!machineCodeFile.is_open())
        {
            cerr << "Error opening machine code file." << endl;
            return;
        }

        string line;
        while (getline(intermediateCodeFile, line))
        {
            vector<string> tokens = split(line, '\t');
            string finalMC = "+";
            if (tokens.size() == 3)
            {
                string address = tokens[0];
                string instruction = tokens[1];
                string operand = tokens[2];

                if (instruction.find("IS") != string::npos)
                {
                    string instrCode = split(instruction, ', ')[1];
                    // cout<<instrCode.substr(0, instrCode.length()-1)<<endl;
                    finalMC += instrCode.substr(0, instrCode.length() - 1);
                    vector<string> operList = split(operand, ',');
                    string oper1 = operList[0];
                    string oper2T = operList[1];
                    string oper2O = operList[2];
                    finalMC += oper1.substr(1, 1);
                    int ordpos = stoi(oper2O);
                    string type = oper2T.substr(2, 2);
                    string addr = getAddress(type, ordpos);
                    finalMC += getAddress(type, ordpos);
                    // Print or write finalMC to machineCodeFile
                }
                else if (instruction.find("DL") != string::npos)
                {
                    // machineCodeFile << address << " " << instruction << " " << operand << endl;
                    string instrCode = split(instruction, ', ')[1];
                    string ni = instrCode.substr(0, instrCode.length() - 1);
                    if (ni == "01")
                    {
                        vector<string> operList = split(operand, ', ');
                        string value = operList[1];
                        string valuetrim = value.substr(0, 1);
                        finalMC += padWithLeadingZeros(valuetrim);
                    }
                }
                for (auto it = finalMC.begin(); it != finalMC.end(); ++it)
                {
                    // Check if it is at the end of the string
                    if (it == finalMC.end())
                    {
                        machineCodeFile<<endl;
                    }
                    else
                    {
                        machineCodeFile<<it<<" ";
                    }
                }
            }
        }

        intermediateCodeFile.close();
        machineCodeFile.close();
    }
};

int main()
{
    Assembler assembler;

    // Load symbol and literal tables
    assembler.loadSymbolTable("SYMTAB.txt");
    assembler.loadLiteralTable("LTTAB.txt");

    // Run Pass II
    assembler.passII();

    return 0;
}
