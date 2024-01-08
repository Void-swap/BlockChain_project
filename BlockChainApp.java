import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Arrays;

public class BlockChainApp {
    ArrayList<Block> blockChain = new ArrayList<>();
    HashMap<String, Long> userKeys = new HashMap<>();
    HashMap<Long, String> keyToUser = new HashMap<>();

    public static void main(String[] args) {
        BlockChainApp blockchain = new BlockChainApp();
        blockchain.runAuthenticationSystem();
    }

    public void runAuthenticationSystem() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            printMainMenu();
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
                    System.out.println("Exited from the Confidential System.");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    public void printMainMenu() {
        clearScreen();
        System.out.println("╔═════════════════════════════════════════╗");
        System.out.println("║        CONFIDENTIAL SYSTEM              ║");
        System.out.println("╟─────────────────────────────────────────╢");
        System.out.println("║ 1. Encode a message                     ║");
        System.out.println("║ 2. Decode a message                     ║");
        System.out.println("║ 3. Exit                                 ║");
        System.out.println("╚═════════════════════════════════════════╝");
        System.out.print("Enter your choice: ");
    }

    public void registerUser(Scanner sc) {
        clearScreen();
        System.out.println("╔═════════════════════════════════════════╗");
        System.out.println("║             ENCODE MESSAGE              ║");
        System.out.println("╟─────────────────────────────────────────╢");
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();
        System.out.print("Enter Message: ");
        String data = sc.nextLine();

        String[] register = { name, password, data };
        Block registerBlock = new Block(0, register);
        long key = registerBlock.getBlockHash();
        userKeys.put(name, key);
        keyToUser.put(key, name);
        blockChain.add(registerBlock);
        System.out.println("\tSuccessfully Encoded.");
        System.out.println("\tYour key is: " + key);
        System.out.println("\t*** Share with trusted entities only! ***");
        System.out.println("╚═════════════════════════════════════════╝");
        pressEnterToContinue(sc);
    }

    public void retrieveUserByKey(Scanner sc) {
        clearScreen();
        System.out.println("╔═════════════════════════════════════════╗");
        System.out.println("║             DECODE MESSAGE              ║");
        System.out.println("╟─────────────────────────────────────────╢");
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
                        clearScreen();
                        System.out.println("╔═════════════════════════════════════════╗");
                        System.out.println("║        DECODED SUCCESSFULLY            ║");
                        System.out.println("╟─────────────────────────────────────────╢");
                        System.out.println("\tMessage was from: " + username);
                        displayData(username);
                        System.out.println("╚═════════════════════════════════════════╝");
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
        pressEnterToContinue(sc);
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
            }
        }
    }

    public void pressEnterToContinue(Scanner sc) {
        System.out.print("Press Enter to Continue...");
        sc.nextLine();
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}

class Block {
    private int previousHash;
    private String[] transaction;
    private int blockHash;

    public Block(int previousHash, String[] transaction) {
        this.previousHash = previousHash;
        this.transaction = transaction;
        Object[] contains = { Arrays.hashCode(transaction), previousHash };
        this.blockHash = Arrays.hashCode(contains);
    }

    public int getPreviousHash() {
        return previousHash;
    }

    public String[] getTransaction() {
        return transaction;
    }

    public int getBlockHash() {
        return blockHash;
    }
}
