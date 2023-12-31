package service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import controller.ArticleDAO;
import controller.ClientDAO;
import controller.CommandeDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Article;
import model.Client;
import model.Commande;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

/**
 * Servlet implementation class CommandeServlet
 */
public class CommandeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	CommandeDAO cmdmanage=new CommandeDAO();

    /**
     * Default constructor. 
     */
    public CommandeServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method 
		PrintWriter out =response.getWriter();
		String id = request.getParameter("id");
		String op = request.getParameter("op");
		String val = request.getParameter("val");
		ClientDAO c =new ClientDAO();
		ArticleDAO art=new ArticleDAO();
		if(op != null) {

			if(op.equals("add")) {
				request.setAttribute("articles", art.afficherArticles()); 
				RequestDispatcher rd = request.getRequestDispatcher("ViewCommande/commande.jsp"); 
				rd.forward(request, response);	
			
			}else if(op.equals("del")) {
				if(cmdmanage.supprimeCommandes(Integer.parseInt(id))) {
					request.setAttribute("datacmd", cmdmanage.afficherCommandes()); 
					RequestDispatcher rd = request.getRequestDispatcher("ViewCommande/list_cmd.jsp"); 
					rd.forward(request, response);
				}
			}else if(op.equals("show")) {
					request.setAttribute("datacmd", cmdmanage.afficherInfosCommande(Integer.parseInt(id))); 
					RequestDispatcher rd = request.getRequestDispatcher("ViewCommande/Infos.jsp"); 
					rd.forward(request, response);
				
			}
			
			else if(op.equals("cl")) {
				String nomclient = request.getParameter("nom");
				if(nomclient != null){
					ArrayList<Client> ar= c.afficherClients();
					List client = ar.stream().filter(i ->i.getNom().equals(nomclient)).collect(Collectors.toList());
					String toJson = new Gson().toJson(client);
					//System.out.print(toJson);
					response.setContentType("application/json");
					response.getWriter().write(toJson);
				}
			}else if(op.equals("ar")) {
				String libelle = request.getParameter("nom");
				if(libelle != null){
					ArrayList<Article> ar= art.afficherArticles();
					List article = ar.stream().filter(i ->i.getLibelle().equals(libelle)).collect(Collectors.toList());
					String toJson = new Gson().toJson(article);
					//System.out.print(toJson);
					response.setContentType("application/json");
					response.getWriter().write(toJson);
				}
			}
			
		}else {
			//System.out.print(cmdmanage.afficherCommandesArticle());
			request.setAttribute("datacmd", cmdmanage.afficherCommandes()); 
			RequestDispatcher rd = request.getRequestDispatcher("ViewCommande/list_cmd.jsp"); 
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String op=request.getParameter("op");
		String listart=request.getParameter("listart");
		String id_client=request.getParameter("idclient");
		String etat=request.getParameter("etat");
		//String stock=request.getParameter("stock");
		PrintWriter out =response.getWriter();
		if(op != null) {
			if(op.equals("addcmd")) {

				Commande cmd=new Commande.CommandeBuilder().setId_client(Integer.parseInt(id_client)).setEtat(etat).build();
				if(cmdmanage.ajouterCommande(cmd,listart)!=null) {
					response.sendRedirect(request.getContextPath() + "/CommandeServlet");
				}else {
					out.println("Ajout Commande pas reussi");

				}
			}
			else if(op.equals("etat")) {
				String idCommande=request.getParameter("id");
				if(cmdmanage.modifieretat(Integer.parseInt(idCommande),etat)!=null) {
					request.setAttribute("datacmd", cmdmanage.afficherInfosCommande(Integer.parseInt(idCommande))); 
					RequestDispatcher rd = request.getRequestDispatcher("ViewCommande/Infos.jsp"); 
					rd.forward(request, response);
				}
			}else {
			 out.print("Page N'exist pas");
			}
		}
	}

}
