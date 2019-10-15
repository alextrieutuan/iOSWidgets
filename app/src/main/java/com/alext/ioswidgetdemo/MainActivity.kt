package com.alext.ioswidgetdemo

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.axonvibe.sojocommon.widget.ioscosplay.UIAlertController
import com.axonvibe.sojocommon.widget.ioscosplay.UILoadingController

class MainActivity : AppCompatActivity() {

    private lateinit var singleDialog: Dialog
    private lateinit var twoOptDialog: Dialog
    private lateinit var loadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        UIAlertController.Builder(this)
            .withTitle("Hello")
            .withMessage("iOS! Welcome to Android world")
            .addAction(
                UIAlertController.UIAlertAction(
                    text = "Single Option Dialog",
                    style = UIAlertController.UIAlertActionStyle.DEFAULT,
                    doAction = {
                        singleDialog = UIAlertController.Builder(this)
                            .withTitle("Hello")
                            .withMessage("I'm single option dialog")
                            .addAction(
                                UIAlertController.UIAlertAction(
                                    text = "OK",
                                    style = UIAlertController.UIAlertActionStyle.DEFAULT
                                )
                            ).buildAsDialog()

                        singleDialog.show()
                    }
                )
            )
            .addAction(
                UIAlertController.UIAlertAction(
                    text = "Two Option Dialog",
                    style = UIAlertController.UIAlertActionStyle.DEFAULT,
                    doAction = {
                        twoOptDialog = UIAlertController.Builder(this)
                            .withTitle("Hello")
                            .withMessage("I'm two options dialog with cancel")
                            .addAction(
                                UIAlertController.UIAlertAction(
                                    text = "OK",
                                    style = UIAlertController.UIAlertActionStyle.DEFAULT
                                )
                            )
                            .addAction(
                                UIAlertController.UIAlertAction(
                                    text = "Cancel",
                                    style = UIAlertController.UIAlertActionStyle.CANCEL
                                )
                            )
                            .buildAsDialog()

                        twoOptDialog.show()
                    }
                )
            )
            .addAction(
                UIAlertController.UIAlertAction(
                    text = "Dialog with destructive",
                    style = UIAlertController.UIAlertActionStyle.DEFAULT,
                    doAction = {
                        twoOptDialog = UIAlertController.Builder(this)
                            .withTitle("Hello")
                            .withMessage("I'm two options dialog with destructive")
                            .addAction(
                                UIAlertController.UIAlertAction(
                                    text = "OK",
                                    style = UIAlertController.UIAlertActionStyle.DEFAULT
                                )
                            )
                            .addAction(
                                UIAlertController.UIAlertAction(
                                    text = "Cancel",
                                    style = UIAlertController.UIAlertActionStyle.DESTRUCTIVE
                                )
                            )
                            .buildAsDialog()

                        twoOptDialog.show()
                    }
                )
            )
            .addAction(
                UIAlertController.UIAlertAction(
                    text = "Loading dialog",
                    style = UIAlertController.UIAlertActionStyle.DEFAULT,
                    doAction = {
                        loadingDialog = UILoadingController(this, true).dialog
                        loadingDialog.show()
                    }
                )
            )
            .addAction(
                UIAlertController.UIAlertAction(
                    text = "Cancel",
                    style = UIAlertController.UIAlertActionStyle.CANCEL
                )
            )
            .addAction(
                UIAlertController.UIAlertAction(
                    text = "Exit app",
                    style = UIAlertController.UIAlertActionStyle.DESTRUCTIVE,
                    doAction = { finish() }
                )
            )
            .show()
    }
}
