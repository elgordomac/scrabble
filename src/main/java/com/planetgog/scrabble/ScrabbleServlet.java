/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planetgog.scrabble;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.*;
import com.planetgog.scrabble.anagram.Anagram;
import com.planetgog.scrabble.words.Board;
import com.planetgog.scrabble.words.Rack;
import com.planetgog.scrabble.words.Scrabble;
import com.planetgog.scrabble.words.Word;
import java.io.BufferedReader;

/**
 *
 * @author gmacdon76
 */
public class ScrabbleServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StringBuilder body = new StringBuilder();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        } catch (Exception e) {
            /*error*/
        }

        System.out.println("Request JSON string :" + body.toString());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");

        // process the request...
        String action = request.getPathInfo();
        PrintWriter out = response.getWriter();

        try {
            if (action != null) {
                if (action.equals("/anagram")) {
                    JsonObject jsonObject = new JsonParser().parse(body.toString()).getAsJsonObject();

                    String anagram = jsonObject.get("anagram").getAsString();
                    Anagram solver = new Anagram();
                    out.println("{\"result\":\"" + solver.solve(anagram) + "\"}");
                } else if (action.equals("/scrabble")) {
                    JsonObject jsonObject = new JsonParser().parse(body.toString()).getAsJsonObject();
                    JsonArray boardArray = jsonObject.get("board").getAsJsonArray();
                    
                    Board board = new Board();
                    int i=0;
                    for(JsonElement row : boardArray) {
                        board.set_row(i++, row.getAsString());
                    }
                    
                    String rackStr = jsonObject.get("rack").getAsString();
                    Rack rack = new Rack();
                    rack.set(rackStr);
                    
                    System.out.println("Ready to start working with board: " + board + ", rack: " + rack);
                    
                    Scrabble scrabble = new Scrabble();
                    Word word = scrabble.run(board, rack);
                    if(word != null) {
                        out.println("{\"success\":\"found " + word + "\"}");                    
                    }
                    else {
                        out.println("{\"error\":\"failed\"}");
                    }                    
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.println("{\"error\":\"unknown action " + action + "\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("{\"error\":\"no action\"}");
            }

        } catch (Exception e) {

        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
