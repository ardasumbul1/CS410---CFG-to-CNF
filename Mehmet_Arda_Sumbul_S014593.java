import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.util.ArrayList;


public class Mehmet_Arda_Sumbul_S014593{
    public ArrayList<String> non_terminals = new ArrayList<String>();
    public ArrayList<String> terminals = new ArrayList<String>();
    public ArrayList<String> rules = new ArrayList<String>();
    public String start = "";
    public String file_name = "";

    public Mehmet_Arda_Sumbul_S014593(String name){
        file_name = name;
    }


    public String read_text_file(){
        String empty_string = "";
            try{
                FileInputStream input_file = new FileInputStream(file_name);

                Scanner in = new Scanner(input_file);

                while(in.hasNext()){
                    String text = in.nextLine();
                    empty_string += text + ";";
                }
                in.close();
            } catch(FileNotFoundException e){
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            return empty_string;
        }
    public void read_and_construct() {

        String string_to_parse = read_text_file();
        String[] elements = string_to_parse.split(";");
        int nonterminal_index = 0;
        int terminal_index = 0;
        int rules_index = 0;
        int start_index = 0;

        for (int i = 0; i<elements.length; i++){
            if(elements[i].equals("NON-TERMINAL")){
                nonterminal_index = i;
            }
            if(elements[i].equals("TERMINAL")){
                terminal_index = i;
            }
            if(elements[i].equals("RULES")){
                rules_index = i;
            }
            if(elements[i].equals("START")){
                start_index = i;
            }
        }

        for(int i=nonterminal_index+1; i<terminal_index; i++){
            this.non_terminals.add(elements[i]);
        }
        for(int i= terminal_index+1; i< rules_index; i++){
            this.terminals.add(elements[i]);
        }
        for(int i= rules_index+1; i< start_index; i++){
            this.rules.add(elements[i]);
        }
        for(int i= start_index+1; i< elements.length; i++){
            this.start = this.start + elements[i];
        }
  
    }
    
    public void add_new_state_S(){
        for(int i = 0; i<rules.size(); i++){
            if(rules.get(i).substring(rules.get(i).indexOf(":")).contains(start)){
                start = "S\'";
                rules.add(start+":S");
            }
        }
    }

    public String remove_term(String str, String letter){
        String str_to_return = str;
        while(str_to_return.contains(letter)){
            String first_half = str_to_return.substring(0,str_to_return.indexOf(letter));
            String second_half = str_to_return.substring(str_to_return.indexOf(letter)+1);
            str_to_return = first_half + second_half;
            return str_to_return;
        }

        return str_to_return;
    }

    public ArrayList<String> remove_identical(ArrayList<String> rules ){
        
        ArrayList<String> new_rules = new ArrayList<String>();
        for (int i = 0; i< rules.size(); i++){
            if(!new_rules.contains(rules.get(i))){
                new_rules.add(rules.get(i));
            }
                
            
        }

        return new_rules;
    }

    public void remove_null_products(){
        ArrayList<String> new_rules = rules;
        ArrayList<String> null_ones = new ArrayList<String>();
        for(int i = 0; i< new_rules.size(); i++){
            String left_hand_side = new_rules.get(i).substring(0,new_rules.get(i).indexOf(":"));
            String right_hand_side = new_rules.get(i).substring(new_rules.get(i).indexOf(":")+1);
            if(left_hand_side.contains("S")){
                continue;
            }
            else{
                if(right_hand_side.contains("e")){
                    null_ones.add(left_hand_side);
                    rules.remove(i);
                    i--;
                }
            }
        }
            for(int a = 0; a<null_ones.size(); a++){
                String null_item = null_ones.get(a);
                for(int k = 0; k<new_rules.size();k ++){
                    String variable = new_rules.get(k).substring(0,new_rules.get(k).indexOf(":"));
                    String terminal = new_rules.get(k).substring(new_rules.get(k).indexOf(":")+1);
                    if(terminal.contains(null_item)){
                        if(terminal.length()==1){
                            rules.add(variable+":"+"e");
                        }
                        else{
                            String temp_terminal = remove_term(terminal, null_item);
                            rules.add(variable+":"+temp_terminal);
                            new_rules.add(variable+":"+temp_terminal);

                        }
                    }
                }
            } 
        rules = remove_identical(rules);
    }

    public ArrayList<String> filter_results(ArrayList<String> list, String letter){
        ArrayList<String> return_list = new ArrayList<String>();
        for(int i = 0; i< list.size(); i++){
            if(list.get(i).substring(0,list.get(i).indexOf(":")).contains(letter)){
                return_list.add(list.get(i).substring(list.get(i).indexOf(":")+1));
            }
        }
        return return_list;
    }

    public void remove_unit_products(){
        for(int i = 0; i<rules.size();i++){
            int index = rules.get(i).indexOf(":");
            String left_hand = rules.get(i).substring(0,index);
            String right_hand = rules.get(i).substring(index+1);
            if(right_hand.length() == 1 & non_terminals.contains(right_hand)){
                rules.remove(i);
                ArrayList<String> filtered_list = filter_results(rules,right_hand);
                for(int k = 0 ; k<filtered_list.size(); k++){
                    rules.add(left_hand + ":"+filtered_list.get(k));
                }
            }    
        }
        rules = remove_identical(rules);
    }
    
    public String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public void replace_prods_morethan2(){
        int alphabet_index = 0;
        for(int i = 0; i<rules.size();i++){
            int index = rules.get(i).indexOf(":");
            String left_hand = rules.get(i).substring(0,index);
            String right_hand = rules.get(i).substring(index+1);
            String first = "";
            String second = "";
            if(right_hand.length() > 2 ){
                for(int k = 0; k <2 ; k++){
                    if(k == 0){
                        while(non_terminals.contains(alphabet[alphabet_index])){
                            alphabet_index++;
                        }
                        first = alphabet[alphabet_index];
                        rules.add(first+":"+right_hand.substring(0, right_hand.length()/2));
                        non_terminals.add(first);
                    }
                    else{
                        while(non_terminals.contains(alphabet[alphabet_index])){
                            alphabet_index++;
                        }
                        second = alphabet[alphabet_index];
                        rules.add(second+":"+right_hand.substring(right_hand.length()/2));
                        non_terminals.add(second);
                    }
                }
                rules.remove(i);
                i--;
                rules.add(left_hand+":"+first+second);
                }
            }
        rules = remove_identical(rules);
    }


    public String find_terminal(ArrayList <String> rules,String terminal){
        String str = "";
        for(int i = 0; i<rules.size();i++){
            int index = rules.get(i).indexOf(":");
            String left_hand = rules.get(i).substring(0,index);
            String right_hand = rules.get(i).substring(index+1);
            if(right_hand.length()==1 & right_hand.contains(terminal) & !left_hand.contains("S")){
                return str+left_hand;
            }
        }
        return str;
    }

    public void replace_terminals(){
        for(int i = 0; i<rules.size();i++){
            int index = rules.get(i).indexOf(":");
            String left_hand = rules.get(i).substring(0,index);
            String right_hand = rules.get(i).substring(index+1);
            if(right_hand.length()>1){
                for(int k=0; k<right_hand.length(); k++){
                    String temp_terminal = right_hand.substring(k,k+1);
                    if(terminals.contains(temp_terminal)){
                        if(find_terminal(rules,temp_terminal).length()==1){
                            String variable = find_terminal(rules,temp_terminal);
                            right_hand = remove_term(right_hand, temp_terminal);
                            rules.remove(i);
                            i--;
                            if(k==0){
                                rules.add(left_hand+":"+variable+right_hand);
                            }
                            else{
                                rules.add(left_hand+":"+right_hand+variable);
                            }
                        }
                    }
                }
            }
            if(right_hand.length()==1 & non_terminals.contains(right_hand)){
                ArrayList<String> variable = filter_results(rules,right_hand);
                rules.remove(i);
                rules.add(left_hand+":"+variable.get(0));
            }
            }
        }






public static void main(String[] args){
    Mehmet_Arda_Sumbul_S014593 obj = new Mehmet_Arda_Sumbul_S014593("G1.txt");
    obj.read_and_construct();
    obj.add_new_state_S();
    System.out.println("NEW STATE IS ADDED");
    System.out.println(obj.rules);
    System.out.println("NULL PRODUCTS ARE REMOVED");
    obj.remove_null_products();
    System.out.println(obj.rules);
    System.out.println("UNIT PRODUCTS ARE REMOVED");
    obj.remove_unit_products();
    System.out.println(obj.rules);
    System.out.println("DERIVATIONS THAT HAVE MORE THAN 2 LENGHT ARE REPLACED");
    obj.replace_prods_morethan2();
    System.out.println(obj.rules);
    System.out.println("LAST STEP(FINAL RESULT)");
    obj.replace_terminals();
    System.out.println(obj.rules);
}
}