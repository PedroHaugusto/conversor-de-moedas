import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Main {
    public static void main(String[] args) {
        try {
            String apiUrl = "https://api.exchangerate.host/latest";

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonObject rates = jsonResponse.getAsJsonObject("rates");
            String[] currencyCodes = {"ARS", "BOB", "BRL", "CLP", "COP", "USD"};
            System.out.println("\nTaxas de câmbio filtradas:");
            for (String code : currencyCodes) {
                if (rates.has(code)) {
                    System.out.println(code + ": " + rates.get(code).getAsDouble());
                }
            }

            Scanner scanner = new Scanner(System.in);
            boolean continueConversion = true;

            while (continueConversion) {
                System.out.println("\n===== Menu de Conversão de Moedas =====");
                System.out.println("1. Converter Moeda");
                System.out.println("2. Sair");
                System.out.print("Escolha uma opção: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        scanner.nextLine();
                        System.out.print("\nInforme a moeda de origem (exemplo: USD): ");
                        String fromCurrency = scanner.nextLine().toUpperCase();
                        System.out.print("Informe a moeda de destino (exemplo: BRL): ");
                        String toCurrency = scanner.nextLine().toUpperCase();
                        System.out.print("Informe o valor a ser convertido: ");
                        double amount = scanner.nextDouble();
                        if (rates.has(fromCurrency) && rates.has(toCurrency)) {
                            double fromRate = rates.get(fromCurrency).getAsDouble();
                            double toRate = rates.get(toCurrency).getAsDouble();
                            double baseAmount = amount / fromRate;
                            double convertedAmount = baseAmount * toRate;
                            System.out.println("\nResultado da conversão:");
                            System.out.println(amount + " " + fromCurrency + " = " + convertedAmount + " " + toCurrency);
                        } else {
                            System.out.println("Uma das moedas informadas não é válida.");
                        }
                        break;

                    case 2:
                        System.out.println("Saindo do programa...");
                        continueConversion = false;
                        break;

                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                        break;
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao processar a resposta JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
