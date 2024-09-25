#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <climits>

using namespace std;

class Process {
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
        if(type == "bt")    bt = value;
        if(type == "at")    at = value;
        if(type == "prior")    prior = value;
        if(type == "remain")    rem_t = value;
        if(type == "ct")    ct = value;
        if(type == "tat")    tat = value;
    }

    int getTime(string type)
    {
        if(type == "at")    return at;
        if(type == "bt")    return bt;
        if(type == "prior")    return prior;
        if(type == "remain")    return rem_t;
        if(type == "ct")    return ct;
        if(type == "tat")    return tat;
    }


};

class Scheduler {
private:
    vector<Process> processes;

public:

    Scheduler()
    {
        cout<<"Enter the no. of processes: ";
        int n;
        cin>>n;
        for(int i=0;i<n;i++)
        {
            int bt, at, prior=0;
            cout<<endl;
            cout<<"Process "<<i+1<<endl;
            cout<<"Enter value for burst time for Process "<<i+1<<" :";
            cin>>bt;
            cout<<"Enter value for arrival time for Process "<<i+1<<" :";
            cin>>at;
            cout<<"Enter priority(lower value higher priority) for Process "<<i+1<<" :";
            cin>>prior;
            this->addProcess(bt, at, prior);
        }
    }
    void addProcess(int bt, int at, int prior) {
        processes.emplace_back(bt, at, prior);
    }

    void FCFS() {
        // sort accn to arrival time
        sort(processes.begin(), processes.end(), [](Process &a, Process &b) {
            return a.getTime("at") < b.getTime("at");
        });

        // check and compare arrival time with curr_time
        int current_time = 0;
        for (auto &p : processes) {
            if (current_time < p.getTime("at")) {
                current_time = p.getTime("at");
            }
            
            p.setTime("ct", current_time + p.getTime("bt"));
            p.setTime("tat", p.getTime("ct") - p.getTime("at"));
            p.setTime("wt", p.getTime("tat") - p.getTime("bt"));
            current_time = p.getTime("ct");
        }
    }

    void SJF_Preemptive() {
        sort(processes.begin(), processes.end(), [](Process &a, Process &b) {
            return a.getTime("at") < b.getTime("at");
        });

        int current_time = 0;
        int completed = 0;
        int n = processes.size();
        while (completed != n) {
            int idx = -1;
            int min_rem_time = INT_MAX;

            for (int i = 0; i < n; i++) {
                if (processes[i].getTime("at") <= current_time && processes[i].getTime("remain") > 0 && processes[i].getTime("remain") < min_rem_time) {
                    min_rem_time = processes[i].getTime("remain");
                    idx = i;
                }
            }

            if (idx != -1) {
                int temp = processes[idx].getTime("remain");
                temp--;
                processes[idx].setTime("remain", temp);
                if (processes[idx].getTime("remain") == 0) {
                    processes[idx].setTime("ct", current_time + 1);
                    processes[idx].setTime("tat", processes[idx].getTime("ct") - processes[idx].getTime("at"));
                    processes[idx].setTime("wt", processes[idx].getTime("tat") - processes[idx].getTime("bt"));
                    completed++;
                }
                current_time++;
            } else {
                current_time++;
            }
        }
    }

    void prior_NonPreemptive() {
        sort(processes.begin(), processes.end(), [](Process &a, Process &b) {
            return a.getTime("at") < b.getTime("bt");
        });

        int current_time = 0;
        for (auto &p : processes) {
            if (current_time < p.getTime("at")) {
                current_time = p.getTime("at");
            }

            auto it = min_element(processes.begin(), processes.end(), [&current_time](Process &a, Process &b) {
                if (a.getTime("at") <= current_time && b.getTime("at") <= current_time) {
                    return a.getTime("prior") < b.getTime("prior");
                }
                return a.getTime("at") < b.getTime("at");
            });

            p = *it;
            processes.erase(it);

            p.setTime("ct", current_time + p.getTime("bt"));
            p.setTime("tat", p.getTime("ct") - p.getTime("at"));
            p.setTime("wt", p.getTime("tat") - p.getTime("bt"));
            current_time = p.getTime("ct");
            processes.push_back(p);
        }
    }

    void RoundRobin(int quantum) {
        sort(processes.begin(), processes.end(), [](Process &a, Process &b) {
            return a.getTime("at") < b.getTime("at");
        });

        queue<Process*> q;
        int current_time = 0;
        int idx = 0;
        while (idx < processes.size() || !q.empty()) {
            while (idx < processes.size() && processes[idx].getTime("at") <= current_time) {
                q.push(&processes[idx]);
                idx++;
            }

            if (!q.empty()) {
                Process *p = q.front();
                q.pop();

                int execute_time = min(quantum, p->getTime("remain"));
                int temp = p->getTime("remain");
                temp -= execute_time;
                p->setTime("remain", temp);
                current_time += execute_time;

                while (idx < processes.size() && processes[idx].getTime("at") <= current_time) {
                    q.push(&processes[idx]);
                    idx++;
                }

                if (p->getTime("remain") > 0) {
                    q.push(p);
                } else {
                    p->setTime("ct", current_time);
                    p->setTime("tat", p->getTime("ct") - p->getTime("at"));
                    p->setTime("wt", p->getTime("tat") - p->getTime("bt"));
                }
            } else {
                current_time++;
            }
        }
    }

    void display() {
        cout << "Process ID\tAT\tBT\tPR\tCT\tTAT\tWT\n";
        // for (auto &p : processes) {
        //     cout << p.pid << "\t" << p.at << "\t" << p.bt << "\t" << p.prior << "\t" << p.ct << "\t" << p.tat << "\t" << p.wt << "\n";
        // }
        for(int i=0;i<processes.size();i++)
        {
            auto &p = processes[i];
            cout<<"Process "<<i+1<<"\t"<<p.getTime("at") << "\t" << p.getTime("bt") << "\t" << p.getTime("prior") << "\t" << p.getTime("ct") << "\t" << p.getTime("tat") << "\t" << p.getTime("wt") << endl;
        }
    }
};

int main() {
    Scheduler scheduler;

    cout << "FCFS Scheduling:\n";
    scheduler.FCFS();
    scheduler.display();

    cout << "\nSJF (Preemptive) Scheduling:\n";
    scheduler.SJF_Preemptive();
    scheduler.display();

    cout << "\nprior (Non-Preemptive) Scheduling:\n";
    scheduler.prior_NonPreemptive();
    scheduler.display();

    cout << "\nRound Robin (Preemptive) Scheduling:\n";
    cout<<"Enter time quantum: ";
    int quantum;
    cin>>quantum;
    scheduler.RoundRobin(quantum);
    scheduler.display();

    return 0;
}
