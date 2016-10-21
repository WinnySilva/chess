

import javax.vecmath.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.behaviors.vp.*;
import javax.swing.JFrame;
import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.lw3d.Lw3dLoader;
import com.sun.j3d.loaders.objectfile.*;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Winny S
 */
public class TrabalhoCG extends JFrame {
    public Canvas3D canvas;
    private SimpleUniverse universe;
    private BranchGroup viewer,movel,imovel;
    private Vector3d[][] board_matrix;
    private TransformGroup[][] pecas_group;
    private TransformGroup light;
    private BoundingSphere bounds;
    private Scene[] pecas, pecas2;
    
    public TrabalhoCG(){
        Transform3D trans = new Transform3D();
        Transform3D trans2 = new Transform3D();
    //  trans2.rotY(Math.PI/4);
        trans.rotX(Math.PI/4);
        trans.setScale(0.4f);
        trans.mul(trans2);
        viewer = new BranchGroup();
        light = new TransformGroup(trans);
        movel = new BranchGroup();
        imovel = new BranchGroup();
        universe = createUniverse();
        try {
            this.importPieces();
            
            this.importBoard();
            this.dispPecas();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TrabalhoCG.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        importLuz();
        
        doCheckPastor();
        
        this.light.addChild(movel);
        this.light.addChild(imovel);
        viewer.addChild(light);
        viewer.compile();
        
        
        
        //universe.addBranchGraph(light);
        universe.addBranchGraph(viewer);
        setSize(700,600);
        getContentPane().add("Center",canvas);
        setVisible(true);
        this.setResizable(false);
    }
    
    private SimpleUniverse createUniverse(){
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());        
        SimpleUniverse uni = new SimpleUniverse(canvas);
        uni.getViewingPlatform().setNominalViewingTransform();
        
        bounds = new BoundingSphere ( new Point3d(0,0,0 ),Double.MAX_VALUE); 
        OrbitBehavior orbit = new OrbitBehavior(canvas);
        orbit.setSchedulingBounds(bounds );
       // uni.getViewingPlatform().setViewPlatformBehavior(orbit);
        
        
        
        TextureLoader textureLoad = new TextureLoader("modelos/floresta.jpg",null);
        
        Background bgImage = new Background(textureLoad.getScaledImage(0.3f, 0.3f ) );
        bgImage.setApplicationBounds(bounds);
        
        AmbientLight ambLight = new AmbientLight( new Color3f(0.7f,0.7f,0.7f) );
        
        
        this.light.addChild(ambLight);
        this.imovel.addChild(bgImage);      
        
        
        return uni;
    }
    
    private void importPieces() throws FileNotFoundException{
        
        ObjectFile obj = new ObjectFile ( );
        pecas = new Scene[16];
        pecas[0]  = (Scene) obj.load( new FileReader(("modelos/Pecas/torre.obj")));//("modelos/AllChess/all.obj")); 
        pecas[1]  = (Scene) obj.load( new FileReader (("modelos/Pecas/cavalo.obj")));
        pecas[2]  = (Scene) obj.load( new FileReader (("modelos/Pecas/bispo.obj")));
        pecas[3]  = (Scene) obj.load( new FileReader (("modelos/Pecas/rei.obj")));
        pecas[4]  = (Scene) obj.load( new FileReader (("modelos/Pecas/rainha.obj")));
        pecas[5]  = (Scene) obj.load( new FileReader (("modelos/Pecas/bispo.obj")));
        pecas[6]  = (Scene) obj.load( new FileReader (("modelos/Pecas/cavalo.obj")));
        pecas[7]  = (Scene) obj.load( new FileReader (("modelos/Pecas/torre.obj")));//("modelos/AllChess/all.obj")); 
        
        for(int i =8; i< 16; i++){
            pecas[i] = (Scene) obj.load( new FileReader (("modelos/Pecas/peao.obj")));
        }
        
        pecas2 = new Scene[16];
        pecas2[0]  = (Scene) obj.load( new FileReader(("modelos/Pecas/torre.obj")));//("modelos/AllChess/all.obj")); 
        pecas2[1]  = (Scene) obj.load( new FileReader (("modelos/Pecas/cavalo.obj")));
        pecas2[2]  = (Scene) obj.load( new FileReader (("modelos/Pecas/bispo.obj")));
        pecas2[3]  = (Scene) obj.load( new FileReader (("modelos/Pecas/rei.obj")));
        pecas2[4]  = (Scene) obj.load( new FileReader (("modelos/Pecas/rainha.obj")));
        pecas2[5]  = (Scene) obj.load( new FileReader (("modelos/Pecas/bispo.obj")));
        pecas2[6]  = (Scene) obj.load( new FileReader (("modelos/Pecas/cavalo.obj")));
        pecas2[7]  = (Scene) obj.load( new FileReader (("modelos/Pecas/torre.obj")));//("modelos/AllChess/all.obj")); 
        
        for(int i =8; i< 16; i++){
            pecas2[i] = (Scene) obj.load( new FileReader (("modelos/Pecas/peao.obj")));
        }
        
        
        
        Material mat2 = new Material(new Color3f(0.5f,0.5f,0.5f),new Color3f(0,0,0),new Color3f(0.5f,0.5f,0.5f),new Color3f(0.5f,0.5f,0.5f),64f  );
        Appearance app = new Appearance();
        app.setMaterial(mat2);
        Material mat = new Material(new Color3f(1f,1f,1f),new Color3f(0.5f,0.5f,0.5f),new Color3f(1,1,1),new Color3f(1f,1,1),64f  );
        Appearance app2 = new Appearance();
        app2.setMaterial(mat);
        Hashtable hash;
        for(int i = 0; i < pecas2.length; i++){
            hash = pecas2[i].getNamedObjects();
            for(Object ob : hash.values()) {
                ((Shape3D) ob ).setAppearance(app);
               
            }
            hash = pecas[i].getNamedObjects();
            for(Object ob : hash.values()) {
                ((Shape3D) ob ).setAppearance(app2);
            }
        }
        this.pecas_group = new TransformGroup[2][16];
        
        
        for(int j=0; j <2; j++){
            for(int i=0; i<16; i++){
                this.pecas_group[j][i] = new TransformGroup();
            }
        }
        
        
    }
    
    
    private void importBoard() throws FileNotFoundException{
        Scene pecas;
        ObjectFile obj = new ObjectFile (ObjectFile.RESIZE);
        pecas  = (Scene) obj.load("modelos/tabuleiro/tabuleiro.obj");
        Hashtable namedObjects = (Hashtable)pecas.getNamedObjects().clone();
        Shape3D board = (Shape3D) namedObjects.get("mesh2");
        TextureLoader textureLoad = new TextureLoader("modelos/tabuleiro/madeira.jpg",null);
        Texture tex = textureLoad.getTexture();
        Appearance app = new Appearance();
        app.setTexture(tex);
        board.setAppearance(app);
        
        Shape3D board2 = (Shape3D) namedObjects.get("mesh1");
        TransparencyAttributes ta1 = new TransparencyAttributes();
        ta1.setTransparencyMode(TransparencyAttributes.BLENDED);
        ta1.setTransparency(0.8f);
        
        
        textureLoad = new TextureLoader("modelos/tabuleiro/textChess.jpg",null);
        tex = textureLoad.getTexture();
        //tex.setCapability(Texture.ALPHA);
        Appearance app2 = new Appearance();
        app2.setTexture(tex);
        app2.setTransparencyAttributes(ta1);
        
        board2.setAppearance(app2);
     
        
        Transform3D transform = new Transform3D();
        Transform3D addTransform = new Transform3D();
        transform.setScale(1.5);
        addTransform.setTranslation(new Vector3d(0, 0,0) );
        
        transform.mul(addTransform );
        
        
        TransformGroup tgPlatform = new TransformGroup(transform);
        tgPlatform.addChild(pecas.getSceneGroup());
        
        
        this.imovel.addChild(tgPlatform);
    }
    private void dispPecas(){
       Transform3D translation = new Transform3D();  
       float xInit = 1.15f;
       float yInit;
       float deltaX = (xInit - 0.5f)/2; 
       this.board_matrix = new Vector3d[8][8];
       yInit = 1.16f;
       for(int l = 0; l<8;l++){
           xInit = 1.15f;
           for(int c =0; c<8; c++){
               this.board_matrix[l][c] = new Vector3d(xInit,yInit,-0.3);
               xInit -=deltaX;
           }
           yInit -=0.33f;
       }
      
        for(int j =0; j<8;j++){
            translation.setTranslation(this.board_matrix[0][j] );
            this.pecas_group[0][j].setTransform(translation);
            translation.setTranslation(this.board_matrix[7][j] );
            this.pecas_group[1][j].setTransform(translation);
            
        }
        for(int j =0; j<8;j++){
            translation.setTranslation(this.board_matrix[1][j] );
            this.pecas_group[0][j+8].setTransform(translation);
            translation.setTranslation(this.board_matrix[6][j] );
            this.pecas_group[1][j+(8)].setTransform(translation);
        }
       
       
       
    }
   
    private void doCheckPastor(){
        
        Transform3D transformInter = new Transform3D();
       
        TransformGroup group;
        Alpha alpha;
        float deltaZ = (float) (((board_matrix[1][0].y) - this.board_matrix[0][0].y))* 1f ;
        int increaseTime = 0;
        int duracao = 2000;
// MOVE PEAO BRANCO
        group = new TransformGroup();
        transformInter.rotZ(0.5*Math.PI);
        alpha = new Alpha(1,Alpha.INCREASING_ENABLE,0,0,duracao,0,0,0,0,0);
        PositionInterpolator movePiece = 
                new PositionInterpolator(alpha,group ,transformInter,0.2f,deltaZ);
        movePiece.setSchedulingBounds(bounds);
       
        group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.pecas_group[0][11].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.movel.setCapabilityIsFrequent(BranchGroup.ALLOW_CHILDREN_WRITE);
        group.addChild(movePiece);
        group.addChild(pecas[11].getSceneGroup());
        this.pecas_group[0][11].addChild(group);
        increaseTime += duracao;
// MOVE PEAO PRETO        
        group = new TransformGroup();
        alpha = new Alpha(1,Alpha.INCREASING_ENABLE,increaseTime,increaseTime,duracao,0,0,0,0,0);
        transformInter.rotZ(0.5*Math.PI);
        movePiece = 
                new PositionInterpolator(alpha,group ,transformInter,0.2f,0.45f);
        movePiece.setSchedulingBounds(bounds);
        group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.pecas_group[1][8].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.movel.setCapabilityIsFrequent(BranchGroup.ALLOW_CHILDREN_WRITE);
        group.addChild(movePiece);
        group.addChild(pecas2[8].getSceneGroup());
        this.pecas_group[1][8].addChild(group);
        increaseTime += duracao;
//MOVER BISPO BRANCO        
        group = new TransformGroup();
        alpha = new Alpha(1,Alpha.INCREASING_ENABLE,increaseTime,increaseTime,duracao,0,0,0,0,0);
        transformInter.rotZ((0.9 + 0.35)*Math.PI);
        movePiece = 
                new PositionInterpolator(alpha,group ,transformInter,0f,1.3f);
        movePiece.setSchedulingBounds(bounds);
        group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.pecas_group[0][2].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.movel.setCapabilityIsFrequent(BranchGroup.ALLOW_CHILDREN_WRITE);
        group.addChild(movePiece);
        group.addChild(pecas[2].getSceneGroup());
        this.pecas_group[0][2].addChild(group);
        increaseTime += duracao;
// MOVE PEAO PRETO
        group = new TransformGroup();
        alpha = new Alpha(1,Alpha.INCREASING_ENABLE,increaseTime,increaseTime,duracao,0,0,0,0,0);
        transformInter.rotZ(0.5*Math.PI);
        movePiece = 
                new PositionInterpolator(alpha,group ,transformInter,0f,0.75f);
        movePiece.setSchedulingBounds(bounds);
        group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.pecas_group[1][15].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.movel.setCapabilityIsFrequent(BranchGroup.ALLOW_CHILDREN_WRITE);
        group.addChild(movePiece);
        group.addChild(pecas2[15].getSceneGroup());
        this.pecas_group[1][15].addChild(group);
        increaseTime += duracao;
// MOVE RAINHA BRANCA       
        TransformGroup queenGroup = new TransformGroup();
        alpha = new Alpha(1,Alpha.INCREASING_ENABLE,increaseTime,increaseTime,10000,0,0,0,0,0);
        transformInter.rotZ(0);
        float[] positions = new float[] {0,(1/4f), (3f/4f),(4/4)};
        Point3f[] points = new Point3f[] {
            new Point3f( 0f, 0f, -0f ),new Point3f(1.35f,-1.35f, -0f),new Point3f(1.35f,-1.35f, -0f),
            new Point3f( 0.65f, -2f, -0f)
        };
        PathInterpolator moveQueen = new PositionPathInterpolator(alpha,queenGroup,transformInter,positions,points);
        moveQueen.setSchedulingBounds(bounds);
        queenGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.pecas_group[0][4].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.movel.setCapabilityIsFrequent(BranchGroup.ALLOW_CHILDREN_WRITE);
        queenGroup.addChild(moveQueen);
        queenGroup.addChild(this.pecas[4].getSceneGroup());
        increaseTime += duracao;
        this.pecas_group[0][4].addChild(queenGroup);
        
// MOVE PEAO PRETO
        group = new TransformGroup();
        alpha = new Alpha(1,Alpha.INCREASING_ENABLE,increaseTime,increaseTime,duracao,0,0,0,0,0);
        transformInter.rotZ(0.5*Math.PI);
        movePiece = 
                new PositionInterpolator(alpha,group ,transformInter,0f,0.45f);
        movePiece.setSchedulingBounds(bounds);
        group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.pecas_group[1][14].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.movel.setCapabilityIsFrequent(BranchGroup.ALLOW_CHILDREN_WRITE);
        group.addChild(movePiece);
        group.addChild(pecas2[14].getSceneGroup());
        this.pecas_group[1][14].addChild(group);
        increaseTime += duracao;
        
//MOVE PEAO PRETO
        group = new TransformGroup();
        alpha = new Alpha(1,Alpha.INCREASING_ENABLE,increaseTime,increaseTime,1000,0,0,0,0,0);
        transformInter.rotY(0.5*Math.PI);
        movePiece = 
                new PositionInterpolator(alpha,group ,transformInter,0f,-10f);
        movePiece.setSchedulingBounds(bounds);
        group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.pecas_group[1][10].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.movel.setCapabilityIsFrequent(BranchGroup.ALLOW_CHILDREN_WRITE);
        group.addChild(movePiece);
        group.addChild(pecas2[10].getSceneGroup());
        this.pecas_group[1][10].addChild(group);
        increaseTime += duracao;
 // #####################END CHEck
        /*
        for(int i=0; i<16; i++){
            if((i!=8) && (i !=15) && (i!=14) && (i!=10) ){
                this.pecas_group[1][i].addChild(pecas2[i].getSceneGroup());
            }
        }
        for(int i=0; i<16; i++){
            if((i!=2) && (i !=4) && (i!=11) ){
                this.pecas_group[0][i].addChild(pecas[i].getSceneGroup());
            }
        } */
       
        //###########3
          for(int i=9; i<16; i++){
            if((i!=8) && (i !=15) && (i!=14) && (i!=10) ){
                this.pecas_group[1][i].addChild(pecas2[i].getSceneGroup());
            }
        }
        for(int i=9; i<16; i++){
            if((i!=2) && (i !=4) && (i!=11) ){
                this.pecas_group[0][i].addChild(pecas[i].getSceneGroup());
            }
        }
            this.pecas_group[1][4].addChild(pecas2[4].getSceneGroup());
            this.pecas_group[1][3].addChild(pecas2[3].getSceneGroup());
            this.pecas_group[0][3].addChild(pecas[3].getSceneGroup());
        //############3
        
        
        
        
        
        
        Transform3D transform2 = new Transform3D();
        transform2.rotX(Math.PI/2);
        TransformGroup pieces = new TransformGroup(transform2);        
        
        for(int i =0; i< 2 ; i++){
            for(int j =0; j<16; j++){
                pieces.addChild(this.pecas_group[i][j]);
            }
        }
        
        BranchGroup bg = new BranchGroup();
        bg.addChild(pieces);
        //this.movel.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        //this.movel.setCapability(BranchGroup.ALLOW_AUTO_COMPUTE_BOUNDS_WRITE);
        this.movel.addChild(bg);
    }
    void importLuz(){
        Scene lamp=null;
        ObjectFile obj = new ObjectFile ();
        obj.setBasePath("modelos/Lamp");
        try {
            lamp  = (Scene) obj.load("modelos/Lamp/lamp.obj");
        } catch (Exception ex) {
          
        }
        Hashtable namedObjects = lamp.getNamedObjects();
        
        
        Material mat = new Material(new Color3f(0,0,0),new Color3f(0,0,0),new Color3f(0,0,0),new Color3f(0,0,0),120f );
        Appearance app2 = new Appearance();
        app2.setMaterial(mat);
        ((Shape3D)namedObjects.get("cone") ).setAppearance(app2);
        TransparencyAttributes ta1 = new TransparencyAttributes();
        ta1.setTransparencyMode(TransparencyAttributes.BLENDED);
        ta1.setTransparency(0.5f);
        mat = new Material(new Color3f(1,1,1),new Color3f(1,1,1),new Color3f(1,1,1),new Color3f(1,1,1),128f );
        app2 = new Appearance();
        app2.setMaterial(mat);
        app2.setTransparencyAttributes(ta1);
        
        ((Shape3D)namedObjects.get("sphere") ).setAppearance(app2);
        
         
        
        Transform3D tr = new Transform3D();
        Transform3D tr3 = new Transform3D();
        tr3.setTranslation(new Vector3f(0,3,0));
        tr.setScale(0.7);
        tr.mul(tr3);
        
        TransformGroup lampgroup = new TransformGroup(tr);
        lampgroup.addChild(lamp.getSceneGroup());
 
        SpotLight spot = new SpotLight(new Color3f(1,1,1),
                                     new Point3f(0.0f,2.0f,0.0f),
                                     new Point3f(0f,2.0f,0f),
                                     new Vector3f(0.0f,0.0f,1.0f),
                                     (float) (Math.PI),
                                     0.0f);
        spot.setInfluencingBounds(bounds);
        TransformGroup luzGroup = new TransformGroup();
        Alpha alpha = new Alpha(-1,Alpha.INCREASING_ENABLE+Alpha.DECREASING_ENABLE,0,0,1500,500,500,1500,500,500);
        Transform3D transformInter = new Transform3D();
        transformInter.rotZ( Math.PI);
       
        float[] positions = new float[] {0,(3/3) };
        Point3f[] points = new Point3f[] {
            new Point3f( -1f, 0f, 0f ),new Point3f(1,0,0)
        };
        Quat4f[] quatis = new Quat4f[]{
            new Quat4f(0f, 0f, 0f, 0f),new Quat4f(0f, 0f, 0f, 0.0f)
        };
        PathInterpolator moveQueen = 
                new RotPosPathInterpolator(alpha,luzGroup,transformInter,positions,quatis,points);
                //new PositionPathInterpolator(alpha,luzGroup,transformInter,positions,points);
        
        
        moveQueen.setSchedulingBounds(this.bounds);
        
        luzGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
       
        luzGroup.addChild(moveQueen);
        luzGroup.addChild(spot);
        
        luzGroup.addChild(lampgroup);
        
        this.movel.addChild(luzGroup);
    }
    
    
    public static void main (String[] args){
        TrabalhoCG trab = new TrabalhoCG();
        
    }
}
