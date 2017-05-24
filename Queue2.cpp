class Queue2
{
public:
	Queue2(void);
	bool IsEmpty();
	bool IsFull(int, double);
	void EnQueue(int);
	int DeQueue();
	~Queue2(void);
private:
	int arr[10];
	int size;
	int last;
	int first;
};
Queue2::Queue2(void)
{
	size = 10;
	last = 0;
	first = 0;
}
Queue2::~Queue2(void)
{
}
bool Queue2::IsEmpty()
{
	if((last)%size==first)
		return true;
	else
		return false;
}
bool Queue2::IsFull(int i, double j)
{
	if((last+1)%size==first)
		return true;
	else
		return false;
}
void Queue2::EnQueue(int data)
{
	if(!Queue2::IsFull()) {
		arr[last] = data;
		last = (last+1)%size;
	}
}
int Queue2::DeQueue()
{
	if(!Queue2::IsEmpty())
	{
		return arr[first++];
		first = (first+1)%size;
	}
}