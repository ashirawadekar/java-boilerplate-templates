package com.template;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.model.Post;
import com.template.resttemplate.ObjectMapperSupplier;

public class HttpClientExample {

    public static String URL = "https://jsonplaceholder.typicode.com";
    public static String STREAM_URL = "https://stream.companieshouse.gov.uk/companies";
    public static String SINGLE_POST_URL = "https://jsonplaceholder.typicode.com/posts/%s";
    public static String KEY = "o4VCj64C9ToROE6cHpR22tS89lF34DnDl5tMCCWC";



    public static void main(String args[]) throws URISyntaxException {

        List<Post> posts = getPosts();
        System.out.println(posts);

//        getStreamingResponse();

        System.out.println(getSinglePost());

        System.out.println(postNewPostExample(new Post(105l, "Title Example", "Body Example")));
        Post updatedPost = new Post();
        updatedPost.setId(1l);
        updatedPost.setTitle("NEW UPDATED TITLE");
        System.out.println(updatePostPutExample(updatedPost));
        System.out.println(updatePostPatchExample(updatedPost));

        System.out.println(getPostWithQueryParams().size());

    }

    private static List<Post> getPosts() {

        List<Post> posts;
        try(CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet get = new HttpGet(URL + "/posts");
            get.addHeader(HttpHeaders.ACCEPT, "application/json");

            HttpResponse response = client.execute(get);

            if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                ObjectMapper objectMapper = ObjectMapperSupplier.getObjectMapper();
                posts = objectMapper.readValue(response.getEntity().getContent(), new TypeReference<List<Post>>() {});
                return posts;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    private static void getStreamingResponse() {
        try(CloseableHttpClient client = HttpClientBuilder.create().build()) {

            HttpGet get = new HttpGet(STREAM_URL);
            get.addHeader(HttpHeaders.AUTHORIZATION, KEY);

            try (InputStream stream = client.execute(get).getEntity().getContent()) {

                BufferedReader buffered = new BufferedReader(new InputStreamReader(new BufferedInputStream(stream)));

                while (true) {
                    String value = buffered.readLine();

                    if(!value.isBlank()) {
                        System.out.printf("Response is: %s ", value);
                        // Confirm best way to close the client
                        client.close();
                    }
                }
            }
        }  catch (IOException e ) {
            e.printStackTrace();
        }
        System.out.println("EXIT");
    }

    private static Post getSinglePost() {

        ObjectMapper objectMapper = ObjectMapperSupplier.getObjectMapper();

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {

            HttpGet get = new HttpGet(String.format(SINGLE_POST_URL, 1));

            HttpResponse response = client.execute(get);
            if( HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                return objectMapper.readValue(response.getEntity().getContent(), Post.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Post postNewPostExample(Post newPost) {

        ObjectMapper objectMapper = ObjectMapperSupplier.getObjectMapper();

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpPost post = new HttpPost(URL + "/posts");
            post.addHeader(HttpHeaders.ACCEPT, "application/json");

            String requestJson = objectMapper.writeValueAsString(newPost);

            StringEntity entity = new StringEntity(requestJson);
            entity.setContentType("application/json");

            post.setEntity(entity);

            HttpResponse response = client.execute(post);

            if(HttpStatus.SC_CREATED == response.getStatusLine().getStatusCode()) {
                return objectMapper.readValue(response.getEntity().getContent(), Post.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Post updatePostPutExample(Post updatedPost) {

        ObjectMapper objectMapper = ObjectMapperSupplier.getObjectMapper();

        try(CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpPut put = new HttpPut(String.format(SINGLE_POST_URL, updatedPost.getId()));
            put.addHeader(HttpHeaders.ACCEPT, "application/json");

            String jsonRequest = objectMapper.writeValueAsString(updatedPost);

            StringEntity entity = new StringEntity(jsonRequest);
            entity.setContentType("application/json");
            put.setEntity(entity);

            HttpResponse response = client.execute(put);
            if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                return objectMapper.readValue(response.getEntity().getContent(), Post.class);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Post updatePostPatchExample(Post updatedPost) {
        ObjectMapper objectMapper = ObjectMapperSupplier.getObjectMapper();

        try(CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpPatch patch = new HttpPatch(String.format(SINGLE_POST_URL, 1));

            String requestJson = objectMapper.writeValueAsString(updatedPost);
            StringEntity entity = new StringEntity(requestJson);
            entity.setContentType("application/json");
            patch.setEntity(entity);

            HttpResponse response = client.execute(patch);
            if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                return objectMapper.readValue(response.getEntity().getContent(), Post.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static List<Post> getPostWithQueryParams() throws URISyntaxException {
        ObjectMapper objectMapper = ObjectMapperSupplier.getObjectMapper();

        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost("jsonplaceholder.typicode.com").setPath("posts")
                .setParameter("userId", "1");

        URI uri = builder.build();

        System.out.println("URL = " + uri.toString());

        try(CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet get = new HttpGet(uri);

            HttpResponse response = client.execute(get);
            if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                return objectMapper.readValue(response.getEntity().getContent(), new TypeReference<List<Post>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return List.of();
    }
}
