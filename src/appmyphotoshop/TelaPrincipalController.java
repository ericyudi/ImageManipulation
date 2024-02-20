
package appmyphotoshop;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class TelaPrincipalController implements Initializable {
    
    @FXML
    private ScrollPane pnscrool;
    @FXML
    private ImageView imageview;
    private Image img;
    private Image imagem;
    private BufferedImage bimag;
    private File file=null;
    private Menu mntransf;
    private int contador=0;
    @FXML
    private Menu mtransf;
    @FXML
    private Menu mimagej;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         mtransf.setDisable(true);
         mimagej.setDisable(true);
    }    

    private void carregarImagem() {
        FileChooser fchooser=new FileChooser();
        fchooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".png","*.png"));
        fchooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".gif","*.gif"));
        fchooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".jpg","*.jpg"));
        fchooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".jpeg","*.jpeg"));
        file =fchooser.showOpenDialog(null);
        if(file!=null) //um arquivo de imagem foi selecionado
        {
            imagem=new Image(file.toURI().toString());
            imageview.setFitWidth(imagem.getWidth());
            imageview.setFitHeight(imagem.getHeight());
            imageview.setImage(imagem);
            bimag=SwingFXUtils.fromFXImage(imagem, null);
            mtransf.setDisable(false);
            mimagej.setDisable(false);
        }
    }
    @FXML
    private void evtTrocaCores(MouseEvent event) {
        //TrocarCores();
    }

    @FXML
    private void evtAbrir(ActionEvent event) {
         carregarImagem();
    }

    @FXML
    private void evtTomCinza(ActionEvent event) {
        TomCinza();
        contador++;
    }

    @FXML
    private void evtDetecBorda(ActionEvent event) {
        ImagePlus imgplus = new ImagePlus();
        imgplus.setImage(bimag);
        ImageProcessor ipr=imgplus.getProcessor();
        ipr.findEdges();
        imageview.setImage(SwingFXUtils.toFXImage(imgplus.getBufferedImage(), null));
        bimag=imgplus.getBufferedImage();
        contador++;
    }

    @FXML
    private void evtSalvarComo(ActionEvent event) {
        FileChooser fchooser=new FileChooser();
        fchooser.setInitialFileName(file.getName());
        file =fchooser.showSaveDialog(null);
        if(file!=null) //um arquivo de imagem foi selecionado
        {
            try{
               ImageIO.write(bimag, "jpg", file);
            }catch(Exception e)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro: "+e.getMessage());
                alert.showAndWait();
            }
        }
        contador = 0;
    }

    @FXML
    private void evtFechar(ActionEvent event) {
        if(contador == 0)
        {
        ((Button)event.getSource()).getScene().getWindow().hide();
        }
        else {
            Stage stage = new Stage();
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setTitle("Save as");
            a.setHeaderText("Confirmação do Save as");
            a.setContentText("Deseja realmente salva-la ? 🤣");
            stage = (Stage) a.getDialogPane().getScene().getWindow();
            if (a.showAndWait().get() == ButtonType.OK) {
                FileChooser fchooser = new FileChooser();
                fchooser.setInitialFileName(file.getName());
                file = fchooser.showSaveDialog(null);
                if (file != null) //um arquivo de imagem foi selecionado
                {
                    try {
                        ImageIO.write(bimag, "jpg", file);
                    } catch (Exception e) {
                        Alert alertt = new Alert(Alert.AlertType.ERROR);
                        alertt.setContentText("Erro: " + e.getMessage());
                        alertt.showAndWait();
                    }
                }
                ((Button) event.getSource()).getScene().getWindow().hide();
                contador = 0;
            } else {
                ((Button) event.getSource()).getScene().getWindow().hide();
            }
        }
    }

    @FXML
    private void evtPretoBranco(ActionEvent event) {
        int aux,pixel[]={0,0,0,0};
        WritableRaster raster=bimag.getRaster();
        
        for(int y=0;y<imagem.getHeight();y++)   //cada linha da imagem
            for(int x=0;x<imagem.getWidth();x++)  //cada coluna da imagem
            {    raster.getPixel(x, y, pixel);
                 aux=(int)(pixel[0]*0.3+pixel[1]*0.59+pixel[2]*0.11);
                 pixel[0]=pixel[1]=pixel[2]=aux;
                 raster.setPixel(x, y, pixel);
            }
        imagem = SwingFXUtils.toFXImage(bimag, null);
        imageview.setImage(imagem);
        contador++;
    }
    @FXML
    private void evtNegativo(ActionEvent event) {
        EfeitoNegativo();
        contador++;
    }

    @FXML
    private void evtEspelhoHorizontal(ActionEvent event) {
        img=imageview.getImage();
        imageview.setImage(ImgHorizontal(img));
        contador++;
    }

    @FXML
    private void evtEspelhoVertical(ActionEvent event) {
        img=imageview.getImage();
        imageview.setImage(ImgVertical(img));
        contador++;
    }

    @FXML
    private void evtDilatação(ActionEvent event) {
        img=imageview.getImage();
        imageview.setImage(dilatacao(img));
        contador++;
    }

    @FXML
    private void evtErosão(ActionEvent event) {
        img=imageview.getImage();
        imageview.setImage(erosao(img));
        contador++;
    }

    @FXML
    private void evtSmooth(ActionEvent event) {
        img=imageview.getImage();
        imageview.setImage(smooth(img));
        contador++;
    }

    @FXML
    private void evtGamma(ActionEvent event) {
        img=imageview.getImage();
        imageview.setImage(gamma(img));
        contador++;
    }
        private void TomCinza()
    {
        int aux,pixel[]={0,0,0,0};
        WritableRaster raster=bimag.getRaster();
        
        for(int y=0;y<imagem.getHeight();y++)   //cada linha da imagem
            for(int x=0;x<imagem.getWidth();x++)  //cada coluna da imagem
            {    raster.getPixel(x, y, pixel);
                 aux=(int)(pixel[0]*0.3+pixel[1]*0.59+pixel[2]*0.11);
                 pixel[0]=pixel[1]=pixel[2]=aux;
                 raster.setPixel(x, y, pixel);
            }
        imagem = SwingFXUtils.toFXImage(bimag, null);
        imageview.setImage(imagem);
    }

    private void EfeitoNegativo()
    {
     int aux,pixel[]={0,0,0,0};
        WritableRaster raster=bimag.getRaster();
        
        for(int y=0;y<imagem.getHeight();y++)   //cada linha da imagem
            for(int x=0;x<imagem.getWidth();x++)  //cada coluna da imagem
            {    raster.getPixel(x, y, pixel);
                 aux=(Math.abs(pixel[0]-255)+(Math.abs(pixel[1]-255))+(Math.abs(pixel[2]-255)));
                 pixel[0]=pixel[1]=pixel[2]=aux;
                 raster.setPixel(x, y, pixel);
            }
        imagem = SwingFXUtils.toFXImage(bimag, null);
        imageview.setImage(imagem);
    }
    private Image ImgHorizontal(Image img) {
        BufferedImage bimagem;
        bimagem = SwingFXUtils.fromFXImage(img, null);
        ImagePlus imgplus=new ImagePlus();
        imgplus.setImage(bimagem);
        ImageProcessor ipr=imgplus.getProcessor();
        ipr.flipHorizontal();
        return SwingFXUtils.toFXImage(imgplus.getBufferedImage(), null);
    }
    private Image ImgVertical(Image img) {
        BufferedImage bimagem;
        bimagem = SwingFXUtils.fromFXImage(img, null);
        ImagePlus imgplus=new ImagePlus();
        imgplus.setImage(bimagem);
        ImageProcessor ipr=imgplus.getProcessor();
        ipr.flipVertical();
        return SwingFXUtils.toFXImage(imgplus.getBufferedImage(), null);
    }
    private Image erosao(Image img)
    {
        BufferedImage bimagem;
        bimagem = SwingFXUtils.fromFXImage(img, null);
        ImagePlus imgplus=new ImagePlus();
        imgplus.setImage(bimagem);
        ImageProcessor ipr=imgplus.getProcessor();
        ipr.erode();
        return SwingFXUtils.toFXImage(imgplus.getBufferedImage(), null);
    }
    private Image dilatacao(Image img) {
        BufferedImage bimagem;
        bimagem = SwingFXUtils.fromFXImage(img, null);
        ImagePlus imgplus=new ImagePlus();
        imgplus.setImage(bimagem);
        ImageProcessor ipr=imgplus.getProcessor();
        ipr.dilate();
        return SwingFXUtils.toFXImage(imgplus.getBufferedImage(), null);
    }
    private Image smooth(Image img) {
        BufferedImage bimagem;
        bimagem = SwingFXUtils.fromFXImage(img, null);
        ImagePlus imgplus=new ImagePlus();
        imgplus.setImage(bimagem);
        ImageProcessor ipr=imgplus.getProcessor();
        ipr.smooth();
        return SwingFXUtils.toFXImage(imgplus.getBufferedImage(), null);
    }
    private Image gamma(Image img) {
        BufferedImage bimagem;
        bimagem = SwingFXUtils.fromFXImage(img, null);
        ImagePlus imgplus=new ImagePlus();
        imgplus.setImage(bimagem);
        ImageProcessor ipr=imgplus.getProcessor();
        ipr.gamma(5);
        return SwingFXUtils.toFXImage(imgplus.getBufferedImage(), null);
    }
}
