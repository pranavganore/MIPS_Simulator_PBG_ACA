package stages;
/*
 * CMSC-611 Advanced Computer Architecture - Final Project (Fall - 2019)
 * Author : Pranav B Ganore [YI73732]
 * pganore1@umbc.edu
*/

public enum Stage_Type
{

    IFSTAGE(0), IDSTAGE(1), EXSTAGE(2), WBSTAGE(3);

    private int id;

    private Stage_Type(int val)
    {
        this.id = val;
    }

    public int getId()
    {
        return id;
    }

}
