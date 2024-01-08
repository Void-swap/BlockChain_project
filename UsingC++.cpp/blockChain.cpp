#include <iostream>
#include <vector>
#include <map>
#include <string>
#include <functional>
#include <ctime>
#include <iomanip>
#include <chrono>

class Block
{
private:
    int previousHash;
    std::vector<std::string> transaction;
    int blockHash;
    std::time_t timestamp;

public:
    Block(int prevHash, const std::vector<std::string> &trans) : previousHash(prevHash), transaction(trans)
    {
        std::hash<std::string> str_hash;
        std::hash<int> int_hash;
        std::size_t combinedHash = 0;

        for (const std::string &s : transaction)
        {
            combinedHash ^= str_hash(s);
        }
        combinedHash ^= int_hash(previousHash);
        blockHash = static_cast<int>(combinedHash);

        timestamp = std::chrono::system_clock::to_time_t(std::chrono::system_clock::now());
    }

    int getPreviousHash() const
    {
        return previousHash;
    }

    std::vector<std::string> getTransaction() const
    {
        return transaction;
    }

    int getBlockHash() const
    {
        return blockHash;
    }

    std::time_t getTimestamp() const
    {
        return timestamp;
    }
};

class BlockChain
{
private:
    std::vector<Block> blockChain;
    std::map<std::string, long> userKeys;
    std::map<long, std::string> keyToUser;

public:
    void runAuthenticationSystem()
    {
        while (true)
        {
            std::cout << "\n-------- CHAIN-LOCK (CONFIDENTIAL SYSTEM) --------" << std::endl;
            std::cout << "\t1. Encode a New Message" << std::endl;
            std::cout << "\t2. Decode an Existing Message" << std::endl;
            std::cout << "\t3. Exit\n"
                      << std::endl;
            std::cout << "   Enter your choice: ";

            int choice;
            std::cin >> choice;
            std::cin.ignore();

            switch (choice)
            {
            case 1:
                registerUser();
                break;
            case 2:
                retrieveUserByKey();
                break;
            case 3:
                std::cout << "Exited from the confidential system." << std::endl;
                return;
            default:
                std::cout << "Invalid choice. Please select a valid option." << std::endl;
            }
        }
    }

    void registerUser()
    {
        std::string name, password, data;
        std::cout << "\tEnter Name: ";
        std::getline(std::cin, name);
        std::cout << "\tEnter Password: ";
        std::getline(std::cin, password);
        std::cout << "\tEnter Message: ";
        std::getline(std::cin, data);

        std::vector<std::string> registerData = {name, password, data};
        Block registerBlock(0, registerData);
        long key = registerBlock.getBlockHash();
        userKeys[name] = key;
        keyToUser[key] = name;
        blockChain.push_back(registerBlock);

        std::cout << "\n\tSuccessfully Encoded" << std::endl;
        std::cout << "\n\tYour key is: " << key << std::endl;
        std::cout << "   *** Share with a trusted entity only! ***" << std::endl;
    }

    void retrieveUserByKey()
    {
        std::cout << "\t\n*** How to DECODE a message ***" << std::endl;
        std::cout << "Get Access to KEY and PASSWORD from the sender entity\n"
                  << std::endl;
        long key;
        std::cout << "\tEnter key (block hash): ";
        std::cin >> key;
        std::cin.ignore();

        if (keyToUser.find(key) != keyToUser.end())
        {
            std::string username = keyToUser[key];
            if (userKeys.find(username) != userKeys.end())
            {
                long storedKey = userKeys[username];
                std::string inputPassword;
                std::cout << "\tEnter your password: ";
                std::getline(std::cin, inputPassword);

                if (storedKey == key)
                {
                    if (blockContainsPassword(key, inputPassword))
                    {
                        std::cout << "\n\tDECODED SUCCESSFULLY" << std::endl;
                        std::cout << "\n\tSender: " << username << std::endl;
                        displayData(username);
                    }
                    else
                    {
                        std::cout << "Invalid password" << std::endl;
                    }
                }
                else
                {
                    std::cout << "Invalid key" << std::endl;
                }
            }
            else
            {
                std::cout << "User not found." << std::endl;
            }
        }
        else
        {
            std::cout << "Key not found." << std::endl;
        }
    }

    bool blockContainsPassword(long key, const std::string &password)
    {
        for (const Block &block : blockChain)
        {
            if (block.getBlockHash() == key)
            {
                std::vector<std::string> transaction = block.getTransaction();
                if (transaction.size() == 3 && transaction[1] == password)
                {
                    return true;
                }
            }
        }
        return false;
    }

    void displayData(const std::string &username)
    {
        for (const Block &block : blockChain)
        {
            std::vector<std::string> transaction = block.getTransaction();
            if (transaction.size() == 3 && transaction[0] == username)
            {
                std::cout << "\tMessage was: " << transaction[2] << std::endl;
                std::cout << "\tTimestamp: " << convertTimestamp(block.getTimestamp()) << " (" << block.getTimestamp() << ")" << std::endl;
            }
        }
    }

    std::string convertTimestamp(const std::time_t &timestamp)
    {
        std::tm tm = *std::localtime(&timestamp);
        char buffer[20];
        std::strftime(buffer, 20, "%Y-%m-%d %H:%M:%S", &tm);
        return std::string(buffer);
    }
};

int main()
{
    BlockChain blockchain;
    blockchain.runAuthenticationSystem();
    return 0;
}
