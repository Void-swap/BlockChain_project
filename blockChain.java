import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Arrays;

public class blockChain {
    ArrayList<Block> blockChain = new ArrayList<>();
    HashMap<String, Long> userKeys = new HashMap<>();
    HashMap<Long, String> keyToUser = new HashMap<>();

    public static void main(String[] args) {
        blockChain blockchain = new blockChain();
        blockchain.runAuthenticationSystem();
    }

    public void runAuthenticationSystem() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n---- CONFIDENTIAL SYSTEM ----");
            System.out.println("1. Encode a message ");
            System.out.println("2. Decode a message ");
            System.out.println("3. Exit\n");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerUser(sc);
                    break;
                case 2:
                    retrieveUserByKey(sc);
                    break;
                case 3:
                    System.out.println("Exited from the confidential system.");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    public void registerUser(Scanner sc) {
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Password : ");
        String password = sc.nextLine();
        System.out.print("Enter Message : ");
        String data = sc.nextLine();

        long timestamp = System.currentTimeMillis(); // Get the current timestamp
        String[] register = {name, password, data};
        Block registerBlock = new Block(0, register, timestamp); // Pass the timestamp
        long key = registerBlock.getBlockHash();
        userKeys.put(name, key);
        keyToUser.put(key, name);
        blockChain.add(registerBlock);
        System.out.println(
                "\tSuccessfully Encoded.\n\tYour key is: " + key + "\n\t*** Share with trusted entity only! ***");
    }

    public void retrieveUserByKey(Scanner sc) {
        System.out.print("\t***how to DECODE a message***\nGet Access to KEY and PASSWORD from sender entity\n\n");
        System.out.print("Enter key (block hash): ");
        long key = sc.nextLong();
        sc.nextLine();

        if (keyToUser.containsKey(key)) {

            String username = keyToUser.get(key);
            if (userKeys.containsKey(username)) {
                long storedKey = userKeys.get(username);
                System.out.print("Enter your password: ");
                String inputPassword = sc.nextLine();

                if (storedKey == key) {
                    if (blockContainsPassword(key, inputPassword)) {
                        System.out.println("\n\tDECODED SUCCESSFULLY\n\n\tMessage was from: " + username);
                        displayData(username);
                    } else {
                        System.out.println("Invalid password");
                    }
                } else {
                    System.out.println("Invalid key");
                }
            } else {
                System.out.println("User not found.");
            }
        } else {
            System.out.println("Key not found.");
        }
    }

    public boolean blockContainsPassword(long key, String password) {
        for (Block block : blockChain) {
            if (block.getBlockHash() == key) {
                String[] transaction = block.getTransaction();
                if (transaction.length == 3 && transaction[1].equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void displayData(String username) {
        for (Block block : blockChain) {
            String[] transaction = block.getTransaction();
            if (transaction.length == 3 && transaction[0].equals(username)) {
                System.out.println("\tMessage was: " + transaction[2]);
                System.out.println("\tTimestamp: " + block.getTimestamp());
            }
        }
    }
}

class Block{
    private int previousHash;
    private String[] transaction;
    private long timestamp; // New field for timestamp

    private int blockHash;

    public Block(int previousHash, String[] transaction, long timestamp) {
        this.previousHash = previousHash;
        this.transaction = transaction;
        this.timestamp = timestamp; // Initialize the timestamp
        Object[] contains = {Arrays.hashCode(transaction), previousHash, timestamp};
        this.blockHash = Arrays.hashCode(contains);
    }

    public int getPreviousHash() {
        return previousHash;
    }

    public String[] getTransaction() {
        return transaction;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getBlockHash() {
        return blockHash;
    }
}
