#include <iostream>
#include <list>
#include <unordered_map>
using namespace std;

class LRUPageReplacement
{
private:
    int capacity;
    list<int> pageList;
    unordered_map<int, list<int>::iterator> pageMap;

public:
    LRUPageReplacement(int cap) : capacity(cap) {}

    void accessPage(int pageNumber)
    {

        if (pageMap.find(pageNumber) != pageMap.end())
        {
            pageList.erase(pageMap[pageNumber]);
            pageList.push_front(pageNumber);
            pageMap[pageNumber] = pageList.begin();
        }
        else
        {

            if (pageList.size() == capacity)
            {
                int lruPage = pageList.back();
                pageList.pop_back();
                pageMap.erase(lruPage);
            }

            pageList.push_front(pageNumber);
            pageMap[pageNumber] = pageList.begin();
        }
    }

    void display() const
    {
        cout << "Current Pages in Frame: ";
        for (const int &page : pageList)
        {
            cout << page << " ";
        }
        cout << endl;
    }
};

int main()
{
    LRUPageReplacement lru(3);

    // cout<<"Entering in sequence 1 2 3 1 4 5"<<endl;

    // lru.accessPage(1);
    // lru.display();

    // lru.accessPage(2);
    // lru.display();

    // lru.accessPage(3);
    // lru.display();

    // lru.accessPage(1);
    // lru.display();

    // lru.accessPage(4);
    // lru.display();

    // lru.accessPage(5);
    // lru.display();

    int page = 0;
    while(page > -1)
    {
        cout<<"Enter page: ";
        cin>>page;
        lru.accessPage(page);
        lru.display();
    }

    return 0;
}
