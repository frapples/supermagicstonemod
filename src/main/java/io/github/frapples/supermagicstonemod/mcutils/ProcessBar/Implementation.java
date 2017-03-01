package io.github.frapples.supermagicstonemod.mcutils.ProcessBar;

import io.github.frapples.supermagicstonemod.SuperThings.SuperStone;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by minecraft on 17-2-28.
 */
public class Implementation {
    public static interface ProcessBarInfoInterface {
        double getPercent();

        void onProcessDone();

        void onUpdated();
    }

    static public class ProcessBarImplementationContainer extends Container
    {
        public double percent = 0;

        static public ProcessBarInfoInterface info = new ProcessBarInfoInterface() {
            public double getPercent() {
                return 0;
            }

            public void onProcessDone() {
            }
            public void onUpdated() {
            }
        };

        public ProcessBarImplementationContainer()
        {
            super();
        }

        @Override
        public boolean canInteractWith(EntityPlayer playerIn)
        {
            // 坑爹啊
            //return new ItemStack(SuperStone.self()).isItemEqual(playerIn.getCurrentEquippedItem());

            return true;
        }

        @Override
        public void detectAndSendChanges()
        {
            super.detectAndSendChanges();

            this.percent = info.getPercent();

            if (this.percent >= 1) {

                this.info.onProcessDone();
            }

            this.info.onUpdated();

            for (ICrafting i : this.crafters)
            {
                i.sendProgressBarUpdate(this, 0, (int) (this.percent * 1000));
            }
        }

        @SideOnly(Side.CLIENT)
        @Override
        public void updateProgressBar(int id, int data)
        {
            super.updateProgressBar(id, data);

            switch (id)
            {
                case 0:
                    this.percent = (double)data / 1000.0;
                    break;
                default:
                    break;
            }
        }


        public double getPercent()
        {
            return this.percent;
        }



        @Override
        public void onContainerClosed(EntityPlayer playerIn) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onContainerClosed(playerIn);
        }
    }

    @SideOnly(Side.CLIENT)
    static public class ProcessBarImplementationGuiContainer extends GuiContainer
    {
        private ProcessBarImplementationContainer container;
        public ProcessBarImplementationGuiContainer(ProcessBarImplementationContainer inventorySlotsIn)
        {
            super(inventorySlotsIn);

            this.container = inventorySlotsIn;
        }

        @Override
        protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
        {
            // TODO
        }

        @Override
        protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
        {

            String title = I18n.format("process_bar.info_str");
            this.fontRendererObj.drawString(
                    title, (this.xSize - this.fontRendererObj.getStringWidth(title)) / 2, 6, 0xFFFFFFFF);


            // this.drawVerticalLine(30, 19, 36, 0xFF000000);
            final int start = 8;
            final int totalLength = 160;

            int length = (int) (this.container.getPercent()  * totalLength);


            this.drawRect(start, 40,  start + length, 43, 0xFFFF9912);
            this.drawRect(start + length, 40, start + totalLength, 43, 0xFF000000);

            //this.drawHorizontalLine(start, start + length, 43, 0xFFFF9912);
            //this.drawHorizontalLine(start + length, start + totalLength, 43, 0xFF000000);
        }
    }

    /**
     * Created by minecraft on 17-2-28.
     */
}
