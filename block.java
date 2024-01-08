import java.util.Arrays;

public class block{
    private int previousHash;
    private String[] transaction;

    private int blockHash;

    public block(int previousHash , String[] transaction){
        this.previousHash=previousHash;
        this.transaction=transaction;

        Object[] contains = {Arrays.hashCode(transaction),previousHash};
        this.blockHash = Arrays.hashCode(contains);
    }

    public int getPreviousHash(){
        return previousHash;
    }

    public String[] getTransaction(){
        return transaction;
    }

    public int getBlockHash(){
        return blockHash;
    }
}