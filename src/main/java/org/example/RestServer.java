package org.example;

import static spark.Spark.*;
import com.google.gson.Gson;

import spark.Request;
import spark.Response;

public class RestServer {

    public static void main(String[] args) {

        DatabaseInit.createTable();

        port(4567);
        Gson gson = new Gson();

        get("/menu", (req, res) -> {
            res.type("application/json");
            return gson.toJson(MenuItemDAO.getAll());
        });

        post("/menu/dish", (req, res) -> {
            Dish dish = gson.fromJson(req.body(), Dish.class);
            MenuItemDAO.add(dish);
            return "Dish added";
        });

        post("/menu/drink", (req, res) -> {
            Drink drink = gson.fromJson(req.body(), Drink.class);
            MenuItemDAO.add(drink);
            return "Drink added";
        });

        delete("/menu/:id", (Request req, Response res) -> {
            int id = Integer.parseInt(req.params(":id"));
            MenuItemDAO.deleteById(id);
            return "Deleted";
        });
    }
}