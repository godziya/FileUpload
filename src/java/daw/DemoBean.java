package daw;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.Part;

@Named
@ViewScoped
public class DemoBean implements Serializable {

    private Part fichero;
    private String nombre;

    /**
     * Sube un fichero al servidor
     *
     * @return boolean devuelve true si cumple expresion regular
     * @throws IOException errir lectura/escritura
     */
    public boolean upload() throws IOException {
        String img = getFilename(fichero);
        if (img.matches("([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)")) {
            final FacesContext facesContext = FacesContext.getCurrentInstance();
            final ExternalContext externalContext = facesContext.getExternalContext();
            //Stream de entrada
            InputStream inputStream = fichero.getInputStream();
            //Fichero de salida
            FileOutputStream outputStream;
            //construimos el fichero en la ruta del servidor /resources/images/
            outputStream = new FileOutputStream(externalContext.getRealPath("/")
                    + "/resources/images/" + getFilename(fichero));

            //Leemos el buffer de entrada y escribimos en nustro fichero.
            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while (true) {
                bytesRead = inputStream.read(buffer);
                if (bytesRead > 0) {
                    outputStream.write(buffer, 0, bytesRead);
                } else {
                    break;
                }
            }
            outputStream.close();
            inputStream.close();

            setNombre(getFilename(fichero));
            return true;
        }
        return false;
    }

    /**
     * Sube un fichero al servidor
     *
     * @param n nombre del fichero
     * @return boolean devuelve true si cumple expresion regular
     * @throws IOException errir lectura/escritura
     */
    public boolean upload(String n) throws IOException {
        String img = getFilename(fichero);     
        if (img.matches("([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)")) {     
            String extension = "";
            //Devuelve el último índice, diviendo por puntos
            int i = getFilename(fichero).lastIndexOf('.'); 
            //Si tiene almenos un punto
            if (i > 0) {
            //Guardamos la extension    
                extension = getFilename(fichero).substring(i + 1);
            }

            final FacesContext facesContext = FacesContext.getCurrentInstance();
            final ExternalContext externalContext = facesContext.getExternalContext();
            //Stream de entrada
            InputStream inputStream = fichero.getInputStream();
            //Fichero de salida
            FileOutputStream outputStream;
            //construimos el fichero en la ruta del servidor /resources/images/
            outputStream = new FileOutputStream(externalContext.getRealPath("/") + "/resources/images/" + n + "." + extension);

            //Leemos el buffer de entrada y escribimos en nustro fichero.
            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while (true) {
                bytesRead = inputStream.read(buffer);
                if (bytesRead > 0) {
                    outputStream.write(buffer, 0, bytesRead);
                } else {
                    break;
                }
            }
            outputStream.close();
            inputStream.close();
            setNombre(n + "." + extension);                 
            return true;
        }
        return false;
    }

    /**
     *
     * @param part
     * @return
     */
    private static String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }

    public Part getFichero() {
        return fichero;
    }

    public void setFichero(Part fichero) {
        this.fichero = fichero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
