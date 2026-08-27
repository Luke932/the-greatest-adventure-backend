package it.matrimonio.backend.service;

import it.matrimonio.backend.model.Guest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${wedding.invite.base-url}")
    private String inviteBaseUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendInvitationEmail(Guest guest) {

        String inviteLink = inviteBaseUrl + "/" + guest.getAccessToken();

        String htmlContent = """
                <!DOCTYPE html>
                <html lang="it">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Il vostro invito al matrimonio</title>
                </head>

                <body style="
                    margin: 0;
                    padding: 0;
                    background-color: #f8f6f2;
                    font-family: Arial, sans-serif;
                    color: #333333;
                ">

                    <div style="
                        max-width: 600px;
                        margin: 40px auto;
                        background-color: #ffffff;
                        border-radius: 12px;
                        overflow: hidden;
                        box-shadow: 0 4px 20px rgba(0,0,0,0.08);
                    ">

                        <div style="
                            padding: 40px 30px;
                            text-align: center;
                            background-color: #faf8f4;
                        ">

                            <h1 style="
                                margin: 0;
                                font-size: 30px;
                                font-weight: normal;
                            ">
                                Il nostro matrimonio
                            </h1>

                            <p style="
                                margin-top: 10px;
                                font-size: 16px;
                                color: #777777;
                            ">
                                Sei ufficialmente invitato!
                            </p>

                        </div>

                        <div style="padding: 40px 30px;">

                            <p style="font-size: 18px;">
                                Ciao <strong>%s</strong>,
                            </p>

                            <p style="font-size: 16px; line-height: 1.6;">
                                siamo felici di invitarti al nostro matrimonio.
                                Abbiamo preparato per te un invito personale.
                            </p>

                            <p style="font-size: 16px; line-height: 1.6;">
                                Attraverso il tuo invito potrai confermare la tua
                                partecipazione e, se necessario, inserire i tuoi
                                accompagnatori.
                            </p>

                            <div style="
                                text-align: center;
                                margin: 35px 0;
                            ">

                                <a href="%s"
                                   style="
                                        display: inline-block;
                                        padding: 15px 30px;
                                        background-color: #333333;
                                        color: #ffffff;
                                        text-decoration: none;
                                        border-radius: 8px;
                                        font-size: 16px;
                                   ">
                                    Apri il tuo invito
                                </a>

                            </div>

                            <p style="
                                font-size: 13px;
                                color: #888888;
                                line-height: 1.5;
                            ">
                                Se il pulsante non dovesse funzionare, puoi
                                copiare e incollare il seguente link nel browser:
                            </p>

                            <p style="
                                font-size: 13px;
                                word-break: break-all;
                            ">
                                %s
                            </p>

                        </div>

                        <div style="
                            padding: 25px;
                            text-align: center;
                            background-color: #faf8f4;
                            color: #888888;
                            font-size: 13px;
                        ">
                            Con affetto,<br>
                            <strong>Gli sposi ❤️</strong>
                        </div>

                    </div>

                </body>
                </html>
                """.formatted(
                guest.getName(),
                inviteLink,
                inviteLink
        );

        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    true,
                    "UTF-8"
            );

            helper.setTo(guest.getEmail());
            helper.setSubject("Il vostro invito al matrimonio 💍");
            helper.setText(htmlContent, true);

            if (fromEmail != null && !fromEmail.isBlank()) {
                helper.setFrom(fromEmail);
            }

            mailSender.send(message);

        } catch (MessagingException exception) {
            throw new RuntimeException(
                    "Errore durante l'invio dell'email di invito",
                    exception
            );
        }
    }
}