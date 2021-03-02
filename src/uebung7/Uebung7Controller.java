package uebung7;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class Uebung7Controller {

    private static final String initialFileName = "rsc/test2.jpg";
    private static File fileOpenPath = new File(".");

    @FXML
    private Slider contrastSlider;

    @FXML
    private Slider coeffSlider;

    @FXML
    private Label contrastLabel;

    @FXML
    private Label coeffLabel;

    @FXML
    private ImageView originalImageView;

    @FXML
    private ImageView DCTImageView;

    @FXML
    private ImageView reconstructedImageView;

    @FXML
    private Label messageLabel;

    @FXML
    private Label entropyOrginal;

    @FXML
    private Label entropyDCT;

    @FXML
    private Label entropyReconstruct;

    @FXML
    private Label MSE;

    @FXML
    void openImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(fileOpenPath);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images (*.jpg, *.png, *.gif)", "*.jpeg", "*.jpg", "*.png", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null) {
            fileOpenPath = selectedFile.getParentFile();

            RasterImage img = new RasterImage(selectedFile);
            img.convertToGray();
            img.setToView(originalImageView);

            processImages();
            messageLabel.getScene().getWindow().sizeToScene();;
        }
    }

    @FXML
    void contrastChanged() {
        int quantization = (int) contrastSlider.getValue();
        contrastLabel.setText("" + quantization);
        processImages();
    }

    @FXML
    void coeffChanged() {
        int coeff = (int) coeffSlider.getValue();
        coeffLabel.setText("" + coeff);
        processImages();
    }

    @FXML
    void filterChanged() {
        processImages();
    }

    @FXML
    public void initialize() {
        // initialize parameters
        contrastChanged();
        coeffChanged();

        RasterImage img = new RasterImage(new File(initialFileName));
        img.convertToGray();
        img.setToView(originalImageView);

        processImages();
    }

    @FXML
    void reset(){
        contrastSlider.setValue(1);
        contrastChanged();
        coeffSlider.setValue(1);
        coeffChanged();
    }

    private void processImages() {
        if(originalImageView.getImage() == null)
            return; // no image: nothing to do

        long startTime = System.currentTimeMillis();

        RasterImage origImg = new RasterImage(originalImageView);
        RasterImage DCTIMG = new RasterImage(origImg.width, origImg.height);
        double[][] dctIMG = new double[origImg.width][origImg.height];
        RasterImage reconstructIMG = new RasterImage(origImg.width, origImg.height);

        DCT.DCTTransformation(origImg, DCTIMG, reconstructIMG,dctIMG);

        DCTIMG.setToView(DCTImageView);
        reconstructIMG.setToView(reconstructedImageView);

        entropyOrginal.setText(String.format("%.3f", DCT.entropy(origImg)));
        entropyDCT.setText(String.format("%.3f", DCT.entropy(DCTIMG)));
        entropyReconstruct.setText(String.format("%.3f", DCT.entropy(reconstructIMG)));

        MSE.setText(String.format("%.2f", DCT.getMSE()));

        messageLabel.setText("Processing time: " + (System.currentTimeMillis() - startTime) + " ms");
    }


}