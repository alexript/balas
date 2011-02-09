package net.autosauler.ballance.client.gui;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Tree;

public interface MenuImages extends Tree.Resources {
    ImageResource adminPanel();
    ImageResource documentsPanel();
    ImageResource financesPanel();
    ImageResource managerPanel();
    ImageResource forAllPanel();
    ImageResource guestPanel();

    /**
     * Use noimage.png, which is a blank 1x1 image.
     */
    @Source("noimage.gif")
    ImageResource treeLeaf();
  }