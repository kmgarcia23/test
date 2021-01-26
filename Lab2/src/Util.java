import java.util.ArrayList;
import java.util.HashMap;

public class Util {

    private String decimalToBinary(int num){
        if (num > 1){
            return decimalToBinary(Math.floorDiv(num, 2)) + String.valueOf(num % 2);
        }
        else{
            return String.valueOf(num);
        }
    }
    // imm =
    // 0 for 16 bit imm
    // 1 for 26 bit address
    // 2 for 5 bit register or shamt
    // 3 for 6 bit opcode or funct
    public String binaryFormater(int num, int imm){
        String str = decimalToBinary(Math.abs(num));
        StringBuilder out = new StringBuilder();
        if(imm == 0){
            for(int i = 0; i< 16 - str.length(); i++){
                out.append("0");
            }
        }
        else if(imm == 1){
            for(int i = 0; i< 26 - str.length(); i++){
                out.append("0");
            }
        }
        else if (imm == 2) {
            for (int i = 0; i < 5 - str.length(); i++) {
                out.append("0");
            }
        }
        else if (imm == 3) {
            for (int i = 0; i < 6 - str.length(); i++) {
                out.append("0");
            }
        }
        else return "Binary Format Error";
        if(num >= 0) return out.toString() + str;
        else {
            String neg = out.toString() + str;
            StringBuilder negout = new StringBuilder();
            for (char bin : neg.toCharArray()) {
                if (bin == '0') negout.append('1');
                else negout.append('0');
            }
            StringBuilder comp2 = new StringBuilder();
            String negout2 = negout.toString();
            boolean addflag = false;
            for (int i = negout2.length() - 1; i >= 0; i--) {
                if(addflag)comp2.append(negout2.charAt(i));

                else if(negout2.charAt(i) == '1') comp2.append('0');
                else{
                    comp2.append('1');
                    addflag = true;
                }
            }
            return comp2.reverse().toString();

        }
    }

    public String registerParser(String reg){
        reg = reg.trim();
        switch (reg.charAt(1)){
            case 'a':
                return binaryFormater(Character.getNumericValue(reg.charAt(2)) + 4, 2);
            case 'v':
                return binaryFormater(Character.getNumericValue(reg.charAt(2)) + 2, 2);
            case 's':
                if(reg.charAt(2) == 'p'){
                    return binaryFormater( 29, 2);
                }
                return binaryFormater(Character.getNumericValue(reg.charAt(2)) + 16, 2);
            case 't':
                if(Character.getNumericValue(reg.charAt(2)) <=7){
                    return binaryFormater(Character.getNumericValue(reg.charAt(2)) + 8, 2);
                }
                else{
                    return binaryFormater(Character.getNumericValue(reg.charAt(2)) + 16, 2);
                }
            case 'r':
                return binaryFormater( 31, 2);
            case 'z': case '0':
                return binaryFormater( 0, 2);
            default:
                return "RegParse Error";

        }
    }

    public Instruction instructionFactory(ArrayList<String> inst, HashMap<String, Integer> label){
        switch (inst.get(0)){
            case "and":
                return new RType(inst.get(1), inst.get(2), inst.get(3), "0", "100100");
            case "or":
                return new RType(inst.get(1), inst.get(2), inst.get(3), "0", "100101");
            case "add":
                return new RType(inst.get(1), inst.get(2), inst.get(3), "0", "100000");
            case "addi":
                return new IType("addi", "001000", inst.get(1), inst.get(2), inst.get(3));
            case "sll":
                return new RType(inst.get(1), inst.get(2), inst.get(3), inst.get(4), "000000");
            case "sub":
                return new RType(inst.get(1), inst.get(2), inst.get(3), "0", "100010");
            case "slt":
                return new RType(inst.get(1), inst.get(2), inst.get(3), "0", "101010");
            case "beq":
                return new IType("beq","000100", inst.get(1), inst.get(2), Integer.toString(label.get(inst.get(3))));
            case "bne":
                return new IType("bne","000101", inst.get(1), inst.get(2), Integer.toString(label.get(inst.get(3))));
            case "lw":
                return new IType("lw", "100011", inst.get(1), inst.get(2), inst.get(3));
            case "sw":
                return new IType("sw", "101011", inst.get(1), inst.get(2), inst.get(3));
            case "j":
                return new JType("000010", inst.get(1));
            case "jr":
                return new RType(inst.get(1), "0", "0", "0", "001000"); //register hard coded to 0
            case "jal":
                return new JType("000011", inst.get(1));
            default:
                System.out.println("invalid instruction: " + inst.get(0));
                System.exit(1);
                return new JType("0","0"); //useless
        }
    }
}
