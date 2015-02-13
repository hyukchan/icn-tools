package com.github.dgautier.icn;

import com.filenet.api.collection.GroupSet;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.security.Group;
import com.filenet.api.security.User;
import com.filenet.api.util.UserContext;
import com.google.common.base.Splitter;
import com.ibm.ecm.extension.PluginServiceCallbacks;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.GZIPOutputStream;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Created by DGA on 21/01/2015.
 */
public class PluginUtils {

    private boolean isUserAuthorized(ObjectStore objectStore,String authorizedGroups) throws Exception {
        User user = Factory.User.fetchCurrent(objectStore.getConnection(),null);


        for (Iterator<String> stringIterator = Splitter.on(",").split(authorizedGroups).iterator();stringIterator.hasNext();){

            String authorizedGroup = stringIterator.next();

            GroupSet userGroups =  user.get_MemberOfGroups();
            Iterator<Group> groupIterator = userGroups.iterator();

            while (groupIterator.hasNext()) {
                Group group = groupIterator.next();

                if (group.get_ShortName().equals(authorizedGroup)){
                    return true;
                }
            }
        }

        return false;
    }

    public static com.filenet.api.core.ObjectStore getObjectStore(String service,ICNLogger logger, PluginServiceCallbacks callbacks, HttpServletRequest request) {
        String repositoryId = request.getParameter(RequestParameters.REPOSITORY_ID);
        String repositoryType = getRepositoryType(request);

        logger.debug(PluginUtils.class, "getObjectStore", "repositoryId=" + repositoryId + "repositoryType=" + repositoryType);
        if (
                (!isNullOrEmpty(repositoryType) && repositoryType.equals("p8"))
                        ||
                        (!isNullOrEmpty(service) && service.startsWith("/p8"))
                ) {

            Subject subject = callbacks.getP8Subject(repositoryId);
            UserContext.get().pushSubject(subject);
            return callbacks.getP8ObjectStore(repositoryId);
        } else {
            return null;
        }


    }

    private static String getRepositoryType(HttpServletRequest request) {
        String repositoryType = request.getParameter(RequestParameters.REPOSITORY_TYPE);

        if (isNullOrEmpty(repositoryType)){
            repositoryType = request.getParameter(RequestParameters.SERVER_TYPE);
        }

        return repositoryType;

    }

    public static void writeResponse(HttpServletRequest request, HttpServletResponse response, String json) throws Exception {
        Writer writer = null;

        try {
            // Prevent browsers from returning cached response on subsequent request
            response.addHeader("Cache-Control", "no-cache");

            // GZip JSON response if client supports it
            String acceptedEncodings = request.getHeader("Accept-Encoding");
            if (acceptedEncodings != null && acceptedEncodings.indexOf("gzip") >= 0) {
                if (!response.isCommitted())
                    response.setBufferSize(65536); // since many times response is larger than default buffer (4096)
                response.setHeader("Content-Encoding", "gzip");
                response.setContentType("text/plain"); // must be text/plain for firebug
                GZIPOutputStream gzos = new GZIPOutputStream(response.getOutputStream());
                writer = new OutputStreamWriter(gzos, "UTF-8");
                // Add secure JSON prefix
                writer.write("{}&&");
                writer.flush();
                writer.write(json);
            } else {
                response.setContentType("text/plain"); // must be text/plain for firebug
                response.setCharacterEncoding("UTF-8");
                writer = response.getWriter();
                // Add secure JSON prefix
                writer.write("{}&&");
                writer.flush();
                writer.write(json);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public static void print(ICNLogger logger, HttpServletRequest request) {

        logger.debug(PluginUtils.class, "print", "Request Headers");
        Enumeration headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String headerName = (String)headerNames.nextElement();
            logger.debug(PluginUtils.class, "print", "headerName="+headerName+";headerValue="+request.getHeader(headerName));
        }

        logger.debug(PluginUtils.class, "print", "Request Parameters");
        Enumeration params = request.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = (String) params.nextElement();
            logger.debug(PluginUtils.class, "print", "paramName="+paramName+";paramValue="+request.getParameter(paramName));
        }
    }
}
