package be.uantwerpen.fti.ei;

public class HelloWorld {
    public HelloWorld(){}
    public void print()
    {
    System.out.println("HelloWorld");
    }

    public void looplHello(int amount)
    {
        int i=0;
        while(i<amount)
        {
            i=i+1;
            print();
        }
    }

}
