/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bungeni.connector.restlet.current;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.util.List;
import org.bungeni.connector.entity.controller.QuestionJpaController;
import org.bungeni.connector.entity.Question;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;

/**
 *
 * @author Dave
 */
public class QuestionsRestlet extends Restlet {

    static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n<package>";
    static final String FOOTER = "\n</package>";

    /**
     * QuestionsRestletHolder is loaded on the first execution of Singleton.getInstance()
     * or the first access to SingletonHolder.INSTANCE, not before.
     */
    private static class QuestionsRestletHolder {

        public static final QuestionsRestlet INSTANCE = new QuestionsRestlet();
    }

    public static QuestionsRestlet getInstance() {
        return QuestionsRestletHolder.INSTANCE;
    }

    @Override
    public void handle(Request request, Response response) {
        try {
            if (request.getMethod().equals(Method.GET)) {
                response.setStatus(Status.SUCCESS_OK);
                response.setStatus(Status.SUCCESS_OK);
                QuestionJpaController questionController = new QuestionJpaController();
                List<Question> questions = questionController.findQuestionEntities();
                XStream xStream = new XStream(new DomDriver());
                xStream.alias(Question.QUESTION_ALIAS, Question.class);
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < questions.size(); i++) {
                    builder.append(xStream.toXML(questions.get(i))).append("\n");
                }
                String members = builder.toString();
                if (!members.isEmpty()) {
                    response.setEntity(HEADER + members + FOOTER, MediaType.TEXT_XML);
                } else {
                }

            } else {
                response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
