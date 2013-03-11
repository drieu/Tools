package fr.dr.svn;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.core.wc2.ISvnObjectReceiver;
import org.tmatesoft.svn.core.wc2.SvnList;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;
import org.tmatesoft.svn.core.wc2.SvnTarget;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Utility class to checkout projects.
 * User: drieu
 * Date: 11/03/13
 * Time: 14:12
 */
public class Checkout {

    private static final Logger logger = Logger.getLogger(Checkout.class);


    private String listFilePath;

    private String propertiesPath;

    private String url;

    private String userRepos;

    private String pwdRepos;

    private String dstDir;

    /**
     * artifactId=version
     */
    private String separator = "=";

    public Checkout(String path, String projectLst) {
        propertiesPath = path;
        listFilePath =  projectLst;
        initProperties();
    }

    /**
     * Load properties file to connect to svn and directory to store the result of checkout.
     */
    public void initProperties() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(propertiesPath));
            url = prop.getProperty("url");
            userRepos = prop.getProperty("user");
            pwdRepos = prop.getProperty("password");
            dstDir =  prop.getProperty("dest");


        } catch (IOException ex) {
            ex.printStackTrace();
        }
        logger.info("Url:" + url + " user:" + userRepos + " pwd:" + pwdRepos + " in destination directory:" + dstDir);
    }





    /**
     * Checkout out a project at a specified version.
     * @param artefact
     * @param version
     */
    public void doCheckout(final String artefact, final String version) {
        SVNURL myUrl = null;
        boolean status = false;

        try {
            myUrl = SVNURL.parseURIEncoded(url + File.separator + artefact + File.separator + "tags" + File.separator + version);
            ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userRepos, pwdRepos );

            SVNUpdateClient svnUp = new SVNUpdateClient(authManager, options);

            File wcDir = new File(dstDir + File.separator + artefact + "_" + version);
            if (!wcDir.exists()) {
                wcDir.mkdirs( );
                logger.info("Checkout for " + myUrl.toString() + "...");
                long val = svnUp.doCheckout(myUrl, wcDir, SVNRevision.HEAD, SVNRevision.HEAD, true);
                if(val>=0) {
                    status = true;
                }

                logger.info("Ici status ...");

            }

        } catch (SVNException e) {
            e.printStackTrace();
        }
        logger.info("Checkout for " + myUrl.toString() + " STATUS:" + status);
        checkoutTagsBeginWithSameVersion(artefact, version);

    }

    /**
     * Specific project methods : Checkout tags with name beginning with same version.
     */
    private void checkoutTagsBeginWithSameVersion(String artefact, String version) {
        if (!checkoutIsOk(dstDir + File.separator + artefact + "_" + version)) {
            logger.info("Pas OK");
            List<String> lstDir = listSvnDirectory(artefact);
            for(String str : lstDir) {
                logger.debug("str:" + str);
                if (str.contains(version)) {
                    logger.info("Try to checkout with:" + str);
                    doCheckout(artefact, str);
                }
            }
            logger.info("End Pas OK");
        } else {
            logger.info("OK");
        }
    }

    private boolean checkoutIsOk(String coDir) {
        boolean res = false;
        File wcDir = new File(coDir);
        String[] arr = wcDir.list();
        if (arr.length != 0) {
            res = true;
        }
        return res;
    }

    /**
     * List the contents of a svn directory.
     * @param artefact
     */
    public List<String> listSvnDirectory(String artefact) {
        logger.info("Retreiev tag list ...");
        final List<String> lst = new ArrayList<String>();
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userRepos, pwdRepos );
        final SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
        svnOperationFactory.setAuthenticationManager(authManager);

        final SvnList list = svnOperationFactory.createList();

        try {
            list.setSingleTarget(SvnTarget.fromURL(SVNURL.parseURIEncoded(url + File.separator + artefact + File.separator + "tags")));
            list.setReceiver(new ISvnObjectReceiver<SVNDirEntry>() {
                public void receive(SvnTarget target, SVNDirEntry object) throws SVNException {
                    logger.debug("=====>object name :" + object.getName());
                    lst.add(object.getName());
                }
            });
            list.run();
        } catch (SVNException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        logger.info("end ...");
        return lst;

    }


    /**
     * By using properties file which contains project and version, we build a svn url and checkout all projects.
     */
    public void doAllCheckout() {
        try {
            FileInputStream fstream = new FileInputStream(listFilePath);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null)   {
                logger.info("===>" + strLine);
                if ((strLine != null) && (strLine.contains(separator)) && (!strLine.contains("#"))) {
                    String arr[] = strLine.split(separator);
                    String artefact = arr[0];
                    String version = arr[1];

                    logger.info("Artefact name :" + artefact + " version:" + version);
                    doCheckout(artefact,version);
                }
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
