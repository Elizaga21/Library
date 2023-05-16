package biblioteca.src.main.java;

import jakarta.persistence.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static EntityManagerFactory emf;
    private static EntityManager em;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        emf = Persistence.createEntityManagerFactory("default");
        em = emf.createEntityManager();

        System.out.println("Bienvenido a la biblioteca");

        do {
            System.out.println("\n1. Nuevo libro");
            System.out.println("2. Modificar libro");
            System.out.println("3. Informe de libro");
            System.out.println("4. Eliminar libro");
            System.out.println("5. Todos los libros");
            System.out.println("6. Número de libros");
            System.out.println("7. Salir");
            System.out.print("Opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    nuevoLibro();
                    break;
                case 2:
                    modificarLibro();
                    break;
                case 3:
                    informeLibro();
                    break;
                case 4:
                    eliminarLibro();
                    break;
                case 5:
                    mostrarLibros();
                    break;
                case 6:
                    numeroLibros();
                    break;
                case 7:
                    System.out.println("Hasta luego!");
                    break;
                default:
                    System.out.println("Opción incorrecta");
            }

        } while (opcion != 7);

        em.close();
        emf.close();
    }

    private static void nuevoLibro() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nIntroduce los datos del libro:");
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        System.out.print("Precio: ");
        double precio = scanner.nextDouble();

        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setPrecio(precio);

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(libro);
        tx.commit();

        System.out.println("Libro añadido correctamente");
    }

    private static void modificarLibro() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nIntroduce el ISBN del libro que quieres modificar: ");
        String isbn = scanner.nextLine();

        Libro libro = em.find(Libro.class, isbn);

        if (libro != null) {
            System.out.println("\nIntroduce los nuevos datos del libro:");
            System.out.print("Título (" + libro.getTitulo() + "): ");
            String titulo = scanner.nextLine();
            System.out.print("Autor (" + libro.getAutor() + "): ");
            String autor = scanner.nextLine();
            System.out.print("Precio (" + libro.getPrecio() + "): ");
            double precio = scanner.nextDouble();

            if (!titulo.isEmpty()) {
                libro.setTitulo(titulo);
            }
            if (!autor.isEmpty()) {
                libro.setAutor(autor);
            }
            if (precio != 0) {
                libro.setPrecio(precio);
            }

            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.merge(libro);
            tx.commit();

            System.out.println("Libro modificado correctamente");
        } else {
            System.out.println("No se ha encontrado ningún libro con ese ISBN");
        }
    }

    private static void informeLibro() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nIntroduce el ISBN del libro que quieres buscar: ");
        String isbn = scanner.nextLine();

        Libro libro = em.find(Libro.class, isbn);

        if (libro != null) {
            System.out.println("Título: " + libro.getTitulo());
            System.out.println("Autor: " + libro.getAutor());
            System.out.println("Precio: " + libro.getPrecio());
        } else {
            System.out.println("No se ha encontrado ningún libro con ese ISBN");
        }
    }

    private static void eliminarLibro() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nIntroduce el ISBN del libro que quieres eliminar: ");
        String isbn = scanner.nextLine();

        Libro libro = em.find(Libro.class, isbn);

        if (libro != null) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.remove(libro);
            tx.commit();

            System.out.println("Libro eliminado correctamente");
        } else {
            System.out.println("No se ha encontrado ningún libro con ese ISBN");
        }
    }

    private static void mostrarLibros() {
        TypedQuery<Libro> query = em.createQuery("SELECT l FROM Libro l", Libro.class);
        List<Libro> libros = query.getResultList();

        if (libros.isEmpty()) {
            System.out.println("No hay libros en la biblioteca");
        } else {
            System.out.println("\nLibros en la biblioteca:");
            for (Libro libro : libros) {
                System.out.println(libro.getIsbn() + " - " + libro.getTitulo() + " - " + libro.getAutor() + " - " + libro.getPrecio());
            }
        }
    }

    private static void numeroLibros() {
        Query query = em.createQuery("SELECT COUNT(l) FROM Libro l");
        Long numLibros = (Long) query.getSingleResult();

        System.out.println("\nHay " + numLibros + " libros en la biblioteca");
    }
}