#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <climits>

using namespace std;

class Process
{
private:
    int bt;
    int at;
    int prior;
    int rem_t;
    int ct;
    int wt;
    int tat;

public:
    Process(int bt, int at, int prior) : bt(bt), at(at), prior(prior), rem_t(bt), ct(0), wt(0), tat(0) {}

    void setTime(string type, int value)
    {
        if (type == "bt")
            bt = value;
        if (type == "at")
            at = value;
        if (type == "prior")
            prior = value;
        if (type == "remain")
            rem_t = value;
        if (type == "ct")
            ct = value;
        if (type == "tat")
            tat = value;
        if (type == "wt")
            wt = value;
    }

    int getTime(string type)
    {
        if (type == "at")
            return at;
        if (type == "bt")
            return bt;
        if (type == "prior")
            return prior;
        if (type == "remain")
            return rem_t;
        if (type == "ct")
            return ct;
        if (type == "tat")
            return tat;
        if (type == "wt")
            return wt;
    }
};

class FCFS
{
private:
    vector<Process> processes;

public:
    FCFS()
    {
        cout << "Enter the no. of processes: ";
        int n;
        cin >> n;
        for (int i = 0; i < n; i++)
        {
            int bt, at, prior = 0;
            cout << endl;
            cout << "Process " << i + 1 << endl;
            cout << "Enter value for burst time for Process " << i + 1 << " :";
            cin >> bt;
            cout << "Enter value for arrival time for Process " << i + 1 << " :";
            cin >> at;
            this->processes.emplace_back(bt, at, prior);
        }
    }

    void sort_Processes()
    {
        int i, j;
        int n = processes.size();
        for (i = 0; i < n - 1; i++)
        {
            for (j = 0; j < n - i - 1; j++)
            {
                if (processes[j].getTime("at") > processes[j + 1].getTime("at"))
                {
                    swap(processes[j], processes[j + 1]);
                }
            }
        }
    }

    void show()
    {
        cout << "Process ID" << "\t" << "AT" << "\t" << "BT" << "\t" << "CT" << "\t" << "TAT" << "\t" << "WT" << endl;
        int n = processes.size();
        for (int i = 0; i < n; i++)
        {
            cout << "Process " << i + 1 << "\t" << processes[i].getTime("at") << "\t" << processes[i].getTime("bt") << "\t" << processes[i].getTime("ct") << "\t" << processes[i].getTime("tat") << "\t" << processes[i].getTime("wt") << endl;
        }
    }

    void perform()
    {
        this->sort_Processes();
        int current_time = 0;
        for (auto &p : processes)
        {
            if (current_time < p.getTime("at"))
            {
                current_time = p.getTime("at");
            }

            p.setTime("ct", current_time + p.getTime("bt"));
            p.setTime("tat", p.getTime("ct") - p.getTime("at"));
            p.setTime("wt", p.getTime("tat") - p.getTime("bt"));
            current_time = p.getTime("ct");
        }
    }
};

class Priority_Nonpreemptive
{
private:
    vector<Process> processes;

public:
    Priority_Nonpreemptive()
    {
        cout << "Enter the no. of processes: ";
        int n;
        cin >> n;
        for (int i = 0; i < n; i++)
        {
            int bt, at, prior;
            cout << endl;
            cout << "Process " << i + 1 << endl;
            cout << "Enter value for burst time for Process " << i + 1 << " :";
            cin >> bt;
            cout << "Enter value for arrival time for Process " << i + 1 << " :";
            cin >> at;
            cout << "Enter priority for Process " << i + 1 << " :";
            cin >> prior;
            this->processes.emplace_back(bt, at, prior);
        }
    }

    void show()
    {
        cout << "Process ID" << "\t" << "AT" << "\t" << "BT" << "\t" << "CT" << "\t" << "Pr" << "\t" << "TAT" << "\t" << "WT" << endl;
        int n = processes.size();
        for (int i = 0; i < n; i++)
        {
            cout << "Process " << i + 1 << "\t" << processes[i].getTime("at") << "\t" << processes[i].getTime("bt") << "\t" << processes[i].getTime("ct") << "\t" << processes[i].getTime("prior") << "\t" << processes[i].getTime("tat") << "\t" << processes[i].getTime("wt") << endl;
        }
    }

    void sort_Processes()
    {
        int i, j;
        int n = processes.size();
        for (i = 0; i < n - 1; i++)
        {
            for (j = 0; j < n - i - 1; j++)
            {
                if (processes[j].getTime("at") > processes[j + 1].getTime("at"))
                {
                    swap(processes[j], processes[j + 1]);
                }
            }
        }
    }

    void perform()
    {
        this->sort_Processes();

        int n = processes.size();
        int current_time = 0;
        int completed = 0;

        priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;

        int index = 0;

        while (completed < n)
        {

            while (index < n && processes[index].getTime("at") <= current_time)
            {
                pq.push({processes[index].getTime("prior"), index});
                index++;
            }

            if (!pq.empty())
            {

                auto [priority, idx] = pq.top();
                pq.pop();

                int bt = processes[idx].getTime("bt");

                int ct = current_time + bt;

                processes[idx].setTime("ct", ct);
                processes[idx].setTime("tat", ct - processes[idx].getTime("at"));
                processes[idx].setTime("wt", processes[idx].getTime("tat") - bt);

                current_time = ct;

                completed++;
            }
            else
            {

                current_time = processes[index].getTime("at");
            }
        }
    }
};

class Round_Robin
{
private:
    vector<Process> processes;
    int quantum;

public:
    Round_Robin()
    {
        cout << "Enter the no. of processes: ";
        int n;
        cin >> n;
        for (int i = 0; i < n; i++)
        {
            int bt, at, prior = 0;
            cout << endl;
            cout << "Process " << i + 1 << endl;
            cout << "Enter value for burst time for Process " << i + 1 << " :";
            cin >> bt;
            cout << "Enter value for arrival time for Process " << i + 1 << " :";
            cin >> at;
            this->processes.emplace_back(bt, at, prior);
        }
        cout << endl;
        cout << "Enter Time Quantum: ";
        cin >> quantum;
    }

    void sort_Processes()
    {
        int i, j;
        int n = processes.size();
        for (i = 0; i < n - 1; i++)
        {
            for (j = 0; j < n - i - 1; j++)
            {
                if (processes[j].getTime("at") > processes[j + 1].getTime("at"))
                {
                    swap(processes[j], processes[j + 1]);
                }
            }
        }
    }

    void show()
    {
        cout << "Process ID" << "\t" << "AT" << "\t" << "BT" << "\t" << "CT" << "\t" << "TAT" << "\t" << "WT" << endl;
        int n = processes.size();
        for (int i = 0; i < n; i++)
        {
            cout << "Process " << i + 1 << "\t" << processes[i].getTime("at") << "\t" << processes[i].getTime("bt") << "\t" << processes[i].getTime("ct") << "\t" << processes[i].getTime("tat") << "\t" << processes[i].getTime("wt") << endl;
        }
    }

    void findWaitingTime() {
        int n = processes.size();
        int rem_bt[n];
        for (int i = 0; i < n; i++) 
            rem_bt[i] = processes[i].getTime("bt");

        int t = 0; // Current time

        while (true) {
            bool done = true;

            for (int i = 0; i < n; i++) {
                if (rem_bt[i] > 0) {
                    done = false;

                    if (rem_bt[i] > quantum) {
                        t += quantum;
                        rem_bt[i] -= quantum;
                    } else {
                        t += rem_bt[i];
                        processes[i].setTime("wt", t - processes[i].getTime("bt"));
                        rem_bt[i] = 0;
                    }
                }
            }

            if (done == true)
                break;
        }
    }

    void findTurnAroundTime() {
        int n = processes.size();
        for (int i = 0; i < n; i++) 
            processes[i].setTime("tat", processes[i].getTime("bt") + processes[i].getTime("wt"));
    }

    void perform() {
        sort_Processes();
        findWaitingTime();
        findTurnAroundTime();
        for (int i = 0; i < processes.size(); i++) {
            processes[i].setTime("ct", processes[i].getTime("tat") + processes[i].getTime("at"));
        }
    }
};

int main()
{
    // FCFS fcfs;
    // fcfs.perform();
    // fcfs.show();

    // Priority_Nonpreemptive p;
    // p.perform();
    // p.show();

    Round_Robin r;
    r.perform();
    r.show();

    return 0;
}