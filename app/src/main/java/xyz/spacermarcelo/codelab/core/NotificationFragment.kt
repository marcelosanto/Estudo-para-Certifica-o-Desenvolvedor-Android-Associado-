package xyz.spacermarcelo.codelab.core

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import xyz.spacermarcelo.codelab.R
import xyz.spacermarcelo.codelab.databinding.FragmentNotificationBinding

private const val NOTIFICATION_ID = 0
private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
private const val ACTION_UPDATE = "ACTION_UPDATE_NOTIFICATION"
private const val ACTION_CANCEL = "ACTION_CANCEL_NOTIFICATION"
private const val ACTION_DELETE_ALL = "ACTION_DELETE_NOTIFICATION"


class NotificationFragment : androidx.fragment.app.Fragment(R.layout.fragment_notification) {

    private lateinit var binding: FragmentNotificationBinding

    private lateinit var notificationManager: NotificationManager
    private val notificationReceiver = NotificationReceiver()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotificationBinding.bind(view)

        setUpButtonListeners()
        setUpUIButtonListeners(enableNotify = true, enableUpdate = false, enableCancel = false)
        createNotificationChannel()
        registerNotificationReceiver()
    }

    private fun setUpButtonListeners() {
        binding.notify.setOnClickListener { sendNotification() }
        binding.update.setOnClickListener { updateNotification() }
        binding.cancel.setOnClickListener { cancelNotification() }
    }

    private fun setUpUIButtonListeners(
        enableNotify: Boolean,
        enableUpdate: Boolean,
        enableCancel: Boolean
    ) {
        binding.notify.isEnabled = enableNotify
        binding.update.isEnabled = enableUpdate
        binding.cancel.isEnabled = enableCancel
    }

    private fun createNotificationChannel() {
        notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Mascot Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableVibration(true)
            notificationChannel.lightColor = Color.RED

            notificationManager.createNotificationChannel(notificationChannel)
        } else {
            // caso o aparelho seja inferior a api 26
        }
    }

    private fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
        setUpUIButtonListeners(enableNotify = true, enableUpdate = false, enableCancel = false)
    }

    private fun updateNotification() {
        // personalização dinamica da notificação adicionando um icone
        val androidImage = BitmapFactory.decodeResource(resources, R.drawable.ic_notification)
        // atualizando o estilo e o titulo
        val notification = getNotificationBuilder()
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(androidImage)
                    .setBigContentTitle("Notificação atualizada!")
            )
        // atualizar a notificação atual
        notificationManager.notify(NOTIFICATION_ID, notification.build())
        // e habilitar o botão de cancelamento
        setUpUIButtonListeners(enableNotify = false, enableUpdate = false, enableCancel = true)
    }

    private fun sendNotification() {
        val builder = getNotificationBuilder()

        createNotificationAction(builder, NOTIFICATION_ID, ACTION_UPDATE, "Atualize")
        createNotificationAction(builder, NOTIFICATION_ID, ACTION_CANCEL, "Remover")

        val deleteAllAction = Intent(ACTION_DELETE_ALL) // remove com slide left/right ou lixeira
        val deletedAction = PendingIntent.getBroadcast(
            requireContext(),
            NOTIFICATION_ID,
            deleteAllAction,
            PendingIntent.FLAG_ONE_SHOT
        )
        builder.setDeleteIntent(deletedAction)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
        // como neste passo aqui a notificação foi enviada, eu deshabilito o botão de enviar
        // e habilito os botões de customização e cancelamento
        setUpUIButtonListeners(enableNotify = false, enableUpdate = true, enableCancel = true)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createNotificationAction(
        builder: NotificationCompat.Builder,
        notificationId: Int,
        actionId: String,
        actionTitle: String
    ) {
        val updateActionFilter = Intent(actionId) // for broadcast receiver
        val updateAction = PendingIntent.getBroadcast(
            requireContext(),
            notificationId,
            updateActionFilter,
            PendingIntent.FLAG_ONE_SHOT
        )
        builder.addAction(
            // mudanças nas notificação desde o Android N
            // esse icone nao aparece mais e esta presente apenas para manter compatibilidade
            // em aparelhos antigos. Em compensação se ganhou mais espaço para os titulos
            // // https://android-developers.googleblog.com/2016/06/notifications-in-android-n.html
            R.drawable.ic_android,
            actionTitle,
            updateAction
        )
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val notificationIntent = Intent(requireContext(), NotificationFragment::class.java)
        val notificationPendingIntent = PendingIntent.getActivity(
            requireContext(),
            NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(requireContext(), PRIMARY_CHANNEL_ID)
            .setContentTitle("Você recebeu uma notificação!")
            .setContentText("Valeu, já vou me inscrever no canal!")
            .setSmallIcon(R.drawable.ic_notification_update)
            .setContentIntent(notificationPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(false)
    }

    private fun registerNotificationReceiver() {
        val notificationActionFilters = IntentFilter()
        notificationActionFilters.addAction(ACTION_UPDATE)
        notificationActionFilters.addAction(ACTION_DELETE_ALL)
        notificationActionFilters.addAction(ACTION_CANCEL)
        requireActivity().registerReceiver(notificationReceiver, notificationActionFilters)
    }

    // for broadcast receiver
    override fun onDestroy() {
        requireActivity().unregisterReceiver(notificationReceiver)
        super.onDestroy()
    }

    inner class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Update the notification
            when (intent.action) {
                ACTION_UPDATE -> updateNotification()
                ACTION_CANCEL -> {
                    notificationManager.cancel(NOTIFICATION_ID)
                    setUpUIButtonListeners(
                        enableNotify = true,
                        enableUpdate = false,
                        enableCancel = false
                    )
                }
                ACTION_DELETE_ALL -> setUpUIButtonListeners(
                    enableNotify = true,
                    enableUpdate = false,
                    enableCancel = false
                )
            }
        }
    }

    // API level 26 a maioria dos broadcastreceiver sao declarados dinamicamente
    private fun registerDynamicReceiver(dynamicReceiver: BroadcastReceiver) {
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            requireActivity().registerReceiver(dynamicReceiver, it)
        }
    }

    /*private fun unregisterDynamicReceiver() {
        requireActivity().unregisterReceiver(dynamicReceiver)
    }*/

    override fun onStop() {
        super.onStop()
        //unregisterDynamicReceiver()
    }

}


