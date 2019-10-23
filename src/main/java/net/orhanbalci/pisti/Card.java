package net.orhanbalci.pisti;

public class Card{
    private CardType type;
    private CardNumber number;

    public Card(CardNumber number, CardType type){
        this.number = number;
        this.type = type;
    }
    
    public CardType getType(){return type;}
    public CardNumber getNumber(){return number;}

}