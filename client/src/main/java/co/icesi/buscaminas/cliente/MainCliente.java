package co.icesi.buscaminas.cliente;

import java.io.IOException;
import java.util.Scanner;

import com.google.gson.Gson;

public class MainCliente {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BuscaminasTCPCliente tcpClient = new BuscaminasTCPCliente();
        Gson gson = new Gson();

        String host = "localhost";
        int port = 12345;

        int option = 0;
        while (option != 6) {
            System.out.println("MENU");
            System.out.println("[1] Iniciar nueva partida (Filas, COlumnas, Minas)");
            System.out.println("[2] Destapar Celda (Fila, Columna)");
            System.out.println("[3] Marcar / Desmarcar bandera *(Fila, Columna )");
            System.out.println("[4] Consultar estado actual del tablero ");
            System.out.println("[5] Rendirse yu revelar tablero completo");
            System.out.println("[6] Salir");
            option = scanner.nextInt();

            if (option == 1) {
                System.out.print("Ingrese número de filas (n): ");
                String n = scanner.next();
                System.out.print("Ingrese número de columnas (m): ");
                String m = scanner.next();
                System.out.print("Ingrese cantidad de minas: ");
                String minas = scanner.next();

                Request req = new Request();
                req.action = "INIT_GAME";
                req.data = new java.util.HashMap<>();
                req.data.put("n", n);
                req.data.put("m", m);
                req.data.put("minas", minas);

                try {
                    Response res = tcpClient.sendRequest(host, port, req);

                    if ("OK".equals(res.status)) {
                        System.out.println("Partida iniciada con éxito.");

                    } else {
                        System.out.println("Error al iniciar: " + res.status);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error de conexión con el servidor.");
                }

            } else if (option == 2) {
                System.out.print("Ingrese fila (i): ");
                String i = scanner.next();
                System.out.print("Ingrese columna (j): ");
                String j = scanner.next();

                Request req = new Request();
                req.action = "SELECT_CELL";
                req.data = new java.util.HashMap<>();
                req.data.put("i", i);
                req.data.put("j", j);

                try {
                    Response res = tcpClient.sendRequest(host, port, req);
                    if ("OK".equals(res.status)) {
                        System.out.println("Celda seleccionada exitosamente.");
                        String boardJson = gson.toJson(res.data.get("board"));
                        Cell[][] board = gson.fromJson(boardJson, Cell[][].class);
                        imprimirTablero(board);
                        Boolean gameEnd = (Boolean) res.data.get("gameEnd");
                        Boolean win = (Boolean) res.data.get("win");

                        if (gameEnd != null && gameEnd) {
                            if (win != null && win) {
                                System.out.println("FELICIDADES GANASTE");
                            } else
                                System.out.println("PERDISTE ");
                        } else {
                            System.out.println(" ");
                        }

                    } else {
                        System.out.println("Error del servidor: " + res.data.get("message"));
                    }
                } catch (IOException e) {
                    System.out.println("Error de conexión con el servidor.");
                }

            } else if (option == 3) {
                System.out.print("Ingrese fila (i): ");
                String i = scanner.next();
                System.out.print("Ingrese columna (j): ");
                String j = scanner.next();

                Request req = new Request();
                req.action = "MARK_CELL";
                req.data = new java.util.HashMap<>();
                req.data.put("i", i);
                req.data.put("j", j);

                try {
                    Response res = tcpClient.sendRequest(host, port, req);
                    if ("OK".equals(res.status)) {
                        System.out.println("Celda marcada exitosamente.");

                    } else {
                        System.out.println("Error del servidor: " + res.data.get("message"));
                    }
                } catch (IOException e) {
                    System.out.println("Error de conexión con el servidor.");
                }

            } else if (option == 4) {
                Request req = new Request();
                req.action = "GET_BOARD";
                req.data = new java.util.HashMap<>();

                try {
                    Response res = tcpClient.sendRequest(host, port, req);
                    if ("OK".equals(res.status)) {
                        String boardJson = gson.toJson(res.data.get("board"));
                        Cell[][] board = gson.fromJson(boardJson, Cell[][].class);
                        imprimirTablero(board);
                    } else {
                        System.out.println("Error del servidor: " + res.data.get("message"));
                    }
                } catch (IOException e) {
                    System.out.println("Error de conexión con el servidor.");
                }
            } else if (option == 5) {
                Request req = new Request();
                req.action = "SOW_ALL";
                req.data = new java.util.HashMap<>();

                try {
                    Response res = tcpClient.sendRequest(host, port, req);
                    if ("OK".equals(res.status)) {
                        String boardJson = gson.toJson(res.data.get("board"));
                        Cell[][] board = gson.fromJson(boardJson, Cell[][].class);
                        imprimirTablero(board);
                    } else {
                        System.out.println("Error del servidor: " + res.data.get("message"));
                    }
                } catch (IOException e) {
                    System.out.println("Error de conexión con el servidor.");
                }
            } else if (option == 6) {
                break;
            } else {
                System.out.println("invalida ");
            }
        }
    }

    public static void imprimirTablero(Cell[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Cell cell = board[i][j];

                if (cell.isMarked()) {
                    System.out.print("[ \u001B[33mM\u001B[0m ] ");
                } else if (cell.isHide() && !cell.isShowAll()) {
                    System.out.print("[ . ] ");
                } else if (cell.isLandMine()) {
                    System.out.print("[ \u001B[31m*\u001B[0m ] ");
                } else {
                    String val = (cell.getValue() == 0) ? " " : String.valueOf(cell.getValue());
                    System.out.print("[ " + val + " ] ");
                }
            }
            System.out.println();
        }
    }
}
