package juc.athome;


/*
* 使用时只要用下面这3行，就能得到值
* CountryEnum.ONE
* CountryEnum.ONE.getRetCode()
* CountryEnum.ONE.getRetMessage()
*
* 也可以用下面方法进行查找
* CountryEnum.forEach_CountryEnum(i).getRetMessage()
* */
public enum CountryEnum {


    ONE(1,"齐")
    ,TWO(2,"楚")
    ,THREE(3,"燕")
    ,FOUR(4,"赵")
    ,FIVE(5,"魏")
    ,SIX(6,"韩");

    CountryEnum(Integer retCode, String retMessage) {
        this.retCode = retCode;
        this.retMessage = retMessage;
    }

    private Integer retCode;
    private String retMessage;

    public Integer getRetCode() {
        return retCode;
    }

    public String getRetMessage() {
        return retMessage;
    }

    public static CountryEnum forEach_CountryEnum(int index) {
        CountryEnum[] myArray = CountryEnum.values();
        for (CountryEnum element: myArray) {
            if(index == element.getRetCode()) {
                return element;
            }
        }
        return null;
    }


}
