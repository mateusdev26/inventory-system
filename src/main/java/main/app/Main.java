package main.app;
import main.domain.Product;
import main.repository.ProductRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
public class Main {
    public static final ProductRepository repository = new ProductRepository();
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int op;
            do {
                menu();
                op = scanner.nextInt();
                switch (op) {
                    case 1:
                        save(scanner);

                        break;

                    case 2:
                        delete(scanner);

                        break;

                    case 3:
                        update(scanner);
                        break;

                    case 4:
                        findById(scanner);
                        break;

                    case 5:
                        findAll(scanner);
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Invalid operation");
                        TimeUnit.SECONDS.sleep(2);
                }

            } while (op != 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




        public static void clear() {
            try {
                final String os = System.getProperty("os.name").toLowerCase();
                if (os.contains("win")) {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                } else {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                System.out.println("\n".repeat(50));
            }
        }
    private static void menu() {
        clear();
        System.out.println("===== PRODUCT MENU =====");
        System.out.println("1. Add product");
        System.out.println("2. Delete product");
        System.out.println("3. Update product");
        System.out.println("4. Find by ID");
        System.out.println("5. List all products");
        System.out.println("0. Exit");
        System.out.print("Select: ");
    }

        private static void findAll(Scanner scanner) {
            clear();
            System.out.println("===========FindAll============");
            if (repository.findAll().isEmpty()){
                System.out.println("Any products in the stock");
            }else {
                repository.findAll().forEach(System.out::println);
            }
            scanner.nextLine();
            scanner.nextLine();
        }

        private static void findById(Scanner scanner) {
            clear();
            System.out.println("==============FindById============");
            long idToFind = scanner.nextLong();
            Product foundById = repository.findById(idToFind);
            if (foundById == null) {
                System.out.println("Any product found");
            } else {
                System.out.println("Product found!!");
                System.out.println(foundById);
            }
        }

        private static void update(Scanner scanner) {
            System.out.println("==========Update============");
            System.out.println("Type the id to update");
            long idUpdate = scanner.nextLong();
            Product product = getProductByScanner(scanner);
            repository.update(idUpdate, product);
        }

        private static void delete(Scanner scanner) throws InterruptedException {
            clear();
            System.out.println("============Delete============");
            System.out.println("Type the id to delete the product");
            long id = scanner.nextLong();
            if (repository.findById(id) == null) {
                System.out.println("Product not found");
                TimeUnit.SECONDS.sleep(2);
            } else {
                System.out.println("Do you wanna to delete ?");
                System.out.println(repository.findById(id));
                System.out.println("Y/n");
                String option = scanner.next();
                if (option.equals("Y") || option.equals("y")) {repository.delete(id);}
            }
        }

        private static void save(Scanner scanner) {
            clear();
            System.out.println("===========Save============");
            Product build = getProductByScanner(scanner);
            repository.save(build);
        }

        private static Product getProductByScanner(Scanner scanner) {
            System.out.println("Type the product name");
            String name = scanner.next();
            System.out.println("Type the product price");
            double price = scanner.nextDouble();
            System.out.println("Type the made date , example : " + LocalDate.now());
            String madeDateString = scanner.next();
            String[] split = madeDateString.split("-");
            LocalDate madeDate = LocalDate.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            System.out.println("Type the expiration date , example : " + LocalDate.now().plusMonths(50));
            String expirationDateString = scanner.next();
            String[] split2 = expirationDateString.split("-");
            LocalDate expirationDate = LocalDate.of(Integer.parseInt(split2[0]), Integer.parseInt(split2[1]), Integer.parseInt(split2[2]));
            System.out.println("Type the country iso3 , example : " + Locale.getDefault().getISO3Country());
            String countryIso3 = scanner.next();
            System.out.println("Type the amount");
            int amount = scanner.nextInt();
            return Product.builder()
                    .name(name)
                    .price(price)
                    .madeDate(madeDate)
                    .expirationDate(expirationDate)
                    .country(countryIso3)
                    .amount(amount)
                    .build();
        }

}
