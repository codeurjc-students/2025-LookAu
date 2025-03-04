package com.codeurjc.backend.model.TicketsTypes;

import java.util.*;

import com.codeurjc.backend.model.EnumTickectType;
import com.codeurjc.backend.model.TicketType;

public class Quinigol extends TicketType {

    private Bet bet1;
    private Bet bet2;
    private Bet bet3;
    private Bet bet4;
    private Bet bet5;
    private Bet bet6;
    private Bet bet7;
    private Bet bet8;

    private EnumTickectType tickectType;



    public Quinigol(Bet bet1, Bet bet2, Bet bet3, Bet bet4, Bet bet5, Bet bet6, Bet bet7, Bet bet8){
        super(EnumTickectType.QUINIGOL);
        this.bet1 = bet1;
        this.bet2 = bet2;
        this.bet3 = bet3;
        this.bet4 = bet4;
        this.bet5 = bet5;
        this.bet6 = bet6;
        this.bet7 = bet7;
        this.bet8 = bet8;
    }

    public Quinigol(List<String> bet1, List<String> bet2, List<String> bet3, List<String> bet4, List<String> bet5, List<String> bet6, List<String> bet7, List<String> bet8){
        super(EnumTickectType.QUINIGOL);
        this.bet1 = new Bet(bet1);
        this.bet2 = new Bet(bet2);;
        this.bet3 = new Bet(bet3);;
        this.bet4 = new Bet(bet4);;
        this.bet5 = new Bet(bet5);;
        this.bet6 = new Bet(bet6);;
        this.bet7 = new Bet(bet7);;
        this.bet8 = new Bet(bet8);;
    }


    public String getTicketTypeName(){
        return "El Quinigol";
    }
    public EnumTickectType getTickectType() {
        return tickectType;
    }

    public Bet getBet1() {
        return bet1;
    }
    public void setBet1(Bet bet1) {
        this.bet1 = bet1;
    }

    public Bet getBet2() {
        return bet2;
    }
    public void setBet2(Bet bet2) {
        this.bet2 = bet2;
    }

    public Bet getBet3() {
        return bet3;
    }
    public void setBet3(Bet bet3) {
        this.bet3 = bet3;
    }

    public Bet getBet4() {
        return bet4;
    }
    public void setBet4(Bet bet4) {
        this.bet4 = bet4;
    }

    public Bet getBet5() {
        return bet5;
    }
    public void setBet5(Bet bet5) {
        this.bet5 = bet5;
    }

    public Bet getBet6() {
        return bet6;
    }
    public void setBet6(Bet bet6) {
        this.bet6 = bet6;
    }

    public Bet getBet7() {
        return bet7;
    }
    public void setBet7(Bet bet7) {
        this.bet7 = bet7;
    }

    public Bet getBet8() {
        return bet8;
    }
    public void setBet8(Bet bet8) {
        this.bet8 = bet8;
    }




    public List<String> getBet1List() {
        return this.bet1.getBet();
    }
    public void setBet1List(List<String> bett) {
        this.bet1.setBet(bett);
    }

    public List<String> getBet2List() {
        return this.bet2.getBet();
    }
    public void setBet2List(List<String> bett) {
        this.bet2.setBet(bett);
    }

    public List<String> getBet3List() {
        return this.bet3.getBet();
    }
    public void setBet3List(List<String> bett) {
        this.bet3.setBet(bett);
    }

    public List<String> getBet4List() {
        return this.bet4.getBet();
    }
    public void setBet4List(List<String> bett) {
        this.bet4.setBet(bett);
    }

    public List<String> getBet5List() {
        return this.bet5.getBet();
    }
    public void setBet5List(List<String> bett) {
        this.bet5.setBet(bett);
    }

    public List<String> getBet6List() {
        return this.bet6.getBet();
    }
    public void setBet6List(List<String> bett) {
        this.bet6.setBet(bett);
    }

    public List<String> getBet7List() {
        return this.bet7.getBet();
    }
    public void setBet7List(List<String> bett) {
        this.bet7.setBet(bett);
    }

    public List<String> getBet8List() {
        return this.bet8.getBet();
    }
    public void setBet8List(List<String> bett) {
        this.bet1.setBet(bett);
    }




    public class Bet {

        private String match1;
        private String match2;
        private String match3;
        private String match4;
        private String match5;
        private String match6;
        private String match7;
        private String match8;
        private String match9;
        private String match10;
        private String match11;
        private String match12;
        private String match13;
        private String match14;
        private String match15;

        //values: 1, X, 2

        public Bet(List<String> matches) {
            this.match1 = matches.get(0);
            this.match2 = matches.get(1);
            this.match3 = matches.get(2);
            this.match4 = matches.get(3);
            this.match5 = matches.get(4);
            this.match6 = matches.get(5);
            this.match7 = matches.get(6);
            this.match8 = matches.get(7);
            this.match9 = matches.get(8);
            this.match10 = matches.get(9);
            this.match11 = matches.get(10);
            this.match12 = matches.get(11);
            this.match13 = matches.get(12);
            this.match14 = matches.get(13);
            this.match15 = matches.get(14);
        }

        public Bet(String match1, String match2, String match3, String match4, String match5, String match6, String match7, String match8, String match9, String match10, String match11, String match12, String match13, String match14, String match15) {
            this.match1 = match1;
            this.match2 = match2;
            this.match3 = match3;
            this.match4 = match4;
            this.match5 = match5;
            this.match6 = match6;
            this.match7 = match7;
            this.match8 = match8;
            this.match9 = match9;
            this.match10 = match10;
            this.match11 = match11;
            this.match12 = match12;
            this.match13 = match13;
            this.match14 = match14;
            this.match15 = match15;
        }

        public Bet(){
    
        }

        public String getMatch1() {
            return match1;
        }

        public void setMatch1(String match1) {
            this.match1 = match1;
        }

        public String getMatch2() {
            return match2;
        }

        public void setMatch2(String match2) {
            this.match2 = match2;
        }

        public String getMatch3() {
            return match3;
        }

        public void setMatch3(String match3) {
            this.match3 = match3;
        }

        public String getMatch4() {
            return match4;
        }

        public void setMatch4(String match4) {
            this.match4 = match4;
        }

        public String getMatch5() {
            return match5;
        }

        public void setMatch5(String match5) {
            this.match5 = match5;
        }

        public String getMatch6() {
            return match6;
        }

        public void setMatch6(String match6) {
            this.match6 = match6;
        }

        public String getMatch7() {
            return match7;
        }

        public void setMatch7(String match7) {
            this.match7 = match7;
        }

        public String getMatch8() {
            return match8;
        }

        public void setMatch8(String match8) {
            this.match8 = match8;
        }

        public String getMatch9() {
            return match9;
        }

        public void setMatch9(String match9) {
            this.match9 = match9;
        }

        public String getMatch10() {
            return match10;
        }

        public void setMatch10(String match10) {
            this.match10 = match10;
        }

        public String getMatch11() {
            return match11;
        }

        public void setMatch11(String match11) {
            this.match11 = match11;
        }

        public String getMatch12() {
            return match12;
        }

        public void setMatch12(String match12) {
            this.match12 = match12;
        }

        public String getMatch13() {
            return match13;
        }

        public void setMatch13(String match13) {
            this.match13 = match13;
        }

        public String getMatch14() {
            return match14;
        }

        public void setMatch14(String match14) {
            this.match14 = match14;
        }

        public String getMatch15() {
            return match15;
        }

        public void setMatch15(String match15) {
            this.match15 = match15;
        }

        public void setBet(List<String> matches){
            this.match1 = matches.get(0);
            this.match2 = matches.get(1);
            this.match3 = matches.get(2);
            this.match4 = matches.get(3);
            this.match5 = matches.get(4);
            this.match6 = matches.get(5);
            this.match7 = matches.get(6);
            this.match8 = matches.get(7);
            this.match9 = matches.get(8);
            this.match10 = matches.get(9);
            this.match11 = matches.get(10);
            this.match12 = matches.get(11);
            this.match13 = matches.get(12);
            this.match14 = matches.get(13);
            this.match15 = matches.get(14);
        }

        public List<String> getBet(){
            return Arrays.asList(
                this.match1, 
                this.match2,
                this.match3,
                this.match4,
                this.match5,
                this.match6,
                this.match7,
                this.match8,
                this.match9,
                this.match10,
                this.match11,
                this.match12,
                this.match13,
                this.match14,
                this.match15
            );
        }
        
    
    }
}



