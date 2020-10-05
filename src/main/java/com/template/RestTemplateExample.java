package com.template;

import java.util.*;

import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.resttemplate.ObjectMapperSupplier;
import com.template.resttemplate.RestTemplateSupplier;
import com.template.model.Post;

/**
 * Examples based on https://jsonplaceholder.typicode.com/ api.
 */
public class RestTemplateExample {

	public static String URL = "https://jsonplaceholder.typicode.com";
	public static String SINGLE_POST_URL = "https://jsonplaceholder.typicode.com/posts/{id}";

	public static void main(String[] args) throws JsonProcessingException, InterruptedException {

//		Get all posts
		List<Post> posts = getPostsExample();
		System.out.println("Received " + posts.size() + " posts");

		//Get single post
		Post post = getSinglePostExample(1);
		System.out.println(post);

		// Post a new post
		Post newPost = new Post(12l, "This is title of new post", "This is body of new post");
		Post customPost = postNewPostExample(newPost);
		System.out.println(customPost);

		//Update an existing post with PUT
		Post updatedPost = new Post(1l,"FOO", "BAR");
		updatedPost.setId(1l);
		Post updatedPostInResponse = updatePostPutExample(updatedPost);
		System.out.println(updatedPostInResponse);

		// DELETE Example
		deletePostExample(1l);

		// Get with query params
		List<Post> postsUsingQueryParams = getPostsWithQueryParam(1l);
		System.out.println("Number of posts " + postsUsingQueryParams.size());

		//Updating a resource with PATCH
//		Post updatedPostWithPatch = new Post();
//		updatedPostWithPatch.setTitle("PATCHED TITLE");
//		Post updatedPostInPatchResponse = updatePostPatchExample(updatedPostWithPatch);
//		System.out.println(updatedPostInPatchResponse);

//		Post post1 = new Post(1l,1l,"fake test", "test fake");
//		Post post2 = new Post(1l,1l,"fake test", "test fake");
//		List<Post> posts = new ArrayList<>();
//		posts.add(post1);
//		posts.add(post2);
//
//		ObjectMapper objectMapper = new ObjectMapper();
//		System.out.println(objectMapper.writeValueAsString(posts));
//		String postStrings = objectMapper.writeValueAsString(posts).replaceAll("1", "2");
//		System.out.println(postStrings);
//
//		List<Post> newPosts = objectMapper.readValue(postStrings, new TypeReference<List<Post>>(){});
//		System.out.println(newPosts);

	}

	private static List<Post> getPostsWithQueryParam(long userId) {
		RestTemplate restTemplate = RestTemplateSupplier.getRestTemplate();

		try {

			UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(URL).path("/posts").queryParam("userId", "1");

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

			HttpEntity entity = new HttpEntity(httpHeaders);

			ResponseEntity<String> response = restTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.GET, entity, String.class);

			ObjectMapper objectMapper = ObjectMapperSupplier.getObjectMapper();

			List<Post> posts = 	objectMapper.readValue(response.getBody(), new TypeReference<List<Post>>() {});
			return posts;
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}


		return List.of();
	}

	private static void deletePostExample(long id) {
		RestTemplate restTemplate = RestTemplateSupplier.getRestTemplate();

		ResponseEntity<Post> response = restTemplate.exchange(SINGLE_POST_URL, HttpMethod.DELETE, null ,Post.class, id);
		if(response.getStatusCode().equals(HttpStatus.OK)) {
			System.out.println("Deleted " + response.getBody());
		}
	}



	private static Post updatePostPatchExample(Post updatedPostWithPatch) {
		RestTemplate restTemplate = RestTemplateSupplier.getRestTemplate();
//		RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
//		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//		requestFactory.setConnectTimeout(10000);
//		requestFactory.setReadTimeout(10000);
//
//		restTemplate.setRequestFactory(requestFactory);

		try {
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

//			HttpEntity<String> httpEntity = new HttpEntity("{ \"title\" : \"This is Patched title\"}", httpHeaders);
			HttpEntity<Post> httpEntity = new HttpEntity(updatedPostWithPatch, httpHeaders);

			ResponseEntity<Post> response = restTemplate.exchange(SINGLE_POST_URL, HttpMethod.PATCH, httpEntity, Post.class, 1l );

			if(response != null) {
				return response.getBody();
			}

		} catch ( HttpClientErrorException | HttpServerErrorException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static Post updatePostPutExample(Post updatedPost) {
		RestTemplate restTemplate = RestTemplateSupplier.getRestTemplate();

		try {
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

			HttpEntity<Post> httpEntity = new HttpEntity(updatedPost, httpHeaders);

//			restTemplate.put(SINGLE_POST_URL, httpEntity, updatedPost.getId());
			ResponseEntity<Post> response = restTemplate.exchange(SINGLE_POST_URL, HttpMethod.PUT, httpEntity, Post.class, updatedPost.getId());
			if(response.getStatusCode().equals(HttpStatus.OK)) {
				return response.getBody();
			}

		} catch ( HttpClientErrorException | HttpServerErrorException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static Post postNewPostExample(Post newPost) {
		RestTemplate restTemplate = RestTemplateSupplier.getRestTemplate();

		try {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Post> entity = new HttpEntity<>(newPost, httpHeaders);

		ResponseEntity<Post> response = restTemplate.postForEntity(URL + "/posts", entity, Post.class);

		if(response.getStatusCode().equals(HttpStatus.CREATED)) {
			return response.getBody();
		}
		} catch ( HttpClientErrorException | HttpServerErrorException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static Post getSinglePostExample(int postId) {
		RestTemplate restTemplate = RestTemplateSupplier.getRestTemplate();

		try {
			ResponseEntity<Post> response = restTemplate.getForEntity(SINGLE_POST_URL, Post.class, postId);

			if (response.getStatusCode().equals(HttpStatus.OK)) {

				return response.getBody();
			}
		} catch ( HttpClientErrorException | HttpServerErrorException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static List<Post> getPostsExample() {
		RestTemplate restTemplate = RestTemplateSupplier.getRestTemplate();

		try {
			ResponseEntity<String> response = restTemplate.getForEntity(URL + "/posts", String.class);

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				ObjectMapper objectMapper = ObjectMapperSupplier.getObjectMapper();

				List<Post> posts = objectMapper.readValue(response.getBody(), new TypeReference<List<Post>>() {});
				return posts;
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return List.of();
	}
}
