package dk.cristi.app.webshop.management.unit_context;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.cristi.app.webshop.management.helpers.DummyTestData;
import dk.cristi.app.webshop.management.models.domain.ProductTypeSpecificationVO;
import dk.cristi.app.webshop.management.models.domain.ProductTypeVO;
import dk.cristi.app.webshop.management.models.entities.ProductType;
import dk.cristi.app.webshop.management.services.CategoryService;
import dk.cristi.app.webshop.management.services.ProductTypeService;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriUtils;

import java.util.*;

import static junit.framework.TestCase.fail;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
//@WebAppConfiguration
public class ProductTypeResourceContextTest {

    // FIXME: 15-Jul-18 When CategoryService can't find category and the ProductTypeResource throws a 404, no body seems to be sent during test (just during test)
    @MockBean
    ProductTypeService productTypeService;
    @MockBean
    CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * The URI prefix of the controller
     */
    private static final String CONTROLLER_URI = "/api/products/types/";
    /**
     * A name used to mock the {@link dk.cristi.app.webshop.management.services.ProductTypeService#findByName(String)} to not find any resource.
     */
    private static final String MISSING_PRODUCT_TYPE_NAME = "some really random name";
    /** Default URI encoding */
    private static final String UTF_8 = "UTF-8";

    @Before
    public void setup() {
        final ProductType productType = DummyTestData.PRODUCT_TYPE();

        when(productTypeService.fetchAll())
                .thenReturn(Collections.singletonList(productType));

        when(productTypeService.findByName(productType.getName()))
                .thenReturn(Optional.of(productType));

        when(productTypeService.findByName(MISSING_PRODUCT_TYPE_NAME))
                .thenReturn(Optional.empty());

        when(productTypeService.save(DummyTestData.PRODUCT_TYPE_FROM_VO()))
                .thenReturn(DummyTestData.PRODUCT_TYPE_FROM_VO());

        when(categoryService.fetchOne(DummyTestData.CATEGORY_1().getId()))
                .thenReturn(Optional.of(DummyTestData.CATEGORY_1()));

        setupTokenCreation();
    }

    //region Auth
    @Autowired
    private JwtTokenStore tokenStore; // This error is not right, cause eventually the bean gets autowired anyway...
    @Autowired
    private DefaultTokenServices tokenServices;
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    private OAuth2RequestFactory auth2RequestFactory;

    private void setupTokenCreation() {
        ClientDetailsService clientDetailsService = new InMemoryClientDetailsService();
        ClientDetails clientDetails = new BaseClientDetails();
        Map<String, ClientDetails> clients = new HashMap<>();
        clients.put("william", clientDetails);
        ((InMemoryClientDetailsService) clientDetailsService).setClientDetailsStore(clients);
        auth2RequestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);

        tokenServices.setTokenEnhancer(jwtAccessTokenConverter);
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenStore(tokenStore);
    }

    private OAuth2AccessToken createToken(String username, String password) {
        // Adjusted from https://stackoverflow.com/questions/18536521/spring-oauth2-manually-creating-an-access-token-in-the-token-store
        Map<String, String> authorizationParameters = new HashMap<>();
        authorizationParameters.put("scope", "read");
        authorizationParameters.put("username", "wtf");
        authorizationParameters.put("client_id", username);
        authorizationParameters.put("grant", "password");

        AuthorizationRequest authorizationRequest;
        try {
            authorizationRequest = auth2RequestFactory.createAuthorizationRequest(authorizationParameters);
        } catch (Exception e) {
            return null;
        }
        OAuth2Request oAuth2Request = auth2RequestFactory.createOAuth2Request(authorizationRequest);
        authorizationRequest.setApproved(true);

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorizationRequest.setAuthorities(authorities);

//        Set<String> resourceIds = new HashSet<>();
//        resourceIds.add("mobile-public");
//        authorizationRequest.setResourceIds(resourceIds);

        // Create principal and auth token
        User userPrincipal = new User(username, password, true, true, true, true, authorities);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);

        OAuth2Authentication authenticationRequest = new OAuth2Authentication(oAuth2Request, authenticationToken);
        authenticationRequest.setAuthenticated(true);

        return tokenServices.createAccessToken(authenticationRequest);
    }
    //endregion

    @Test
    public void fetchAll_NoAccept_ProducesJsonUtf8() throws Exception {
        mockMvc.perform(get(CONTROLLER_URI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));

        verify(productTypeService, times(1))
                .fetchAll();
    }

    @Test
    public void fetchAll_AcceptAll_ProducesJsonUtf8() throws Exception {
        mockMvc.perform(get(CONTROLLER_URI).accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));

        verify(productTypeService, times(1))
                .fetchAll();
    }

    @Test
    public void fetchAll_AcceptsJson_ProducesJsonUtf8() throws Exception {
        mockMvc.perform(get(CONTROLLER_URI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));

        verify(productTypeService, times(1))
                .fetchAll();
    }

    @Test
    public void fetchAll_AcceptsXml_CanNotProduce_DefaultsToJson() throws Exception {
        mockMvc.perform(get(CONTROLLER_URI).accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));

        verify(productTypeService, times(1))
                .fetchAll();
    }

    @Test
    public void fetchOne_NoAccept_ProducesJsonUtf8() throws Exception {
        String name = DummyTestData.PRODUCT_TYPE().getName();
        String descr = DummyTestData.PRODUCT_TYPE().getDescription();

        mockMvc.perform(get(CONTROLLER_URI + name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.description").value(descr))
                .andExpect(jsonPath("$.specifications").isArray());

        verify(productTypeService, times(1))
                .findByName(name);
    }

    @Test
    public void fetchOne_AcceptsAll_ProducesJsonUtf8() throws Exception {
        String name = DummyTestData.PRODUCT_TYPE().getName();
        String descr = DummyTestData.PRODUCT_TYPE().getDescription();

        mockMvc.perform(get(CONTROLLER_URI + name).accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.description").value(descr))
                .andExpect(jsonPath("$.specifications").isArray());

        verify(productTypeService, times(1))
                .findByName(name);
    }

    @Test
    public void fetchOne_AcceptsJson_ProducesJsonUtf8() throws Exception {
        String name = DummyTestData.PRODUCT_TYPE().getName();
        String descr = DummyTestData.PRODUCT_TYPE().getDescription();

        mockMvc.perform(get(CONTROLLER_URI + name).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.description").value(descr))
                .andExpect(jsonPath("$.specifications").isArray());

        verify(productTypeService, times(1))
                .findByName(name);

        // can also accept JSON UTF-8
        mockMvc.perform(get(CONTROLLER_URI + name).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.description").value(descr))
                .andExpect(jsonPath("$.specifications").isArray());

        verify(productTypeService, times(2))
                .findByName(name);
    }

    @Test
    public void fetchOne_AcceptsXml_CanNotProduce_DefaultsToJson() throws Exception {
        String name = DummyTestData.PRODUCT_TYPE().getName();

        // can not produce XML
        mockMvc.perform(get(CONTROLLER_URI + name).accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name").value(name));

        verify(productTypeService, times(1))
                .findByName(name);
    }

    @Test
    public void fetchOne_NotFound() throws Exception {
        String name = MISSING_PRODUCT_TYPE_NAME;

        mockMvc.perform(get(CONTROLLER_URI + name))
                .andExpect(status().isNotFound());

        verify(productTypeService, times(1))
                .findByName(name);
    }

    @Test
    public void postProductType_NoAuth() throws Exception {
        ProductTypeVO productTypeVO = DummyTestData.PRODUCT_TYPE_VO();
        mockMvc.perform(post(CONTROLLER_URI).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(productTypeVO)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("unauthorized"));
    }

    @Test
    public void postProductType_WithWrongRole() throws Exception {
        fail("Not implemented");
    }

    @Test
    public void postProductType_WithRightRole() throws Exception {
        fail("Not implemented");
    }

    @Test
    public void postProductType_ValidData() throws Exception {
        ProductTypeVO postData = DummyTestData.PRODUCT_TYPE_VO();

        OAuth2AccessToken auth = createToken("william", "hispass");
        // If auth is null, that only means that the test used an non-existent user
        String authToken = auth.getValue();

        String locationUri = CONTROLLER_URI + UriUtils.encode(postData.getName(), UTF_8);
        mockMvc.perform(post(CONTROLLER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postData))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, CoreMatchers.containsString(locationUri)));

        verify(productTypeService, times(1))
                .save(any(ProductType.class));
    }

    @Test
    public void postProductType_InvalidData() throws Exception {

        OAuth2AccessToken auth = createToken("william", "hispass");
        // If auth is null, that only means that the test used an non-existent user
        String authToken = auth.getValue();

        // product name can't be null
        ProductTypeVO postData = new ProductTypeVO(null, "some descr", 1, new ProductTypeSpecificationVO[0]);
        mockMvc.perform(post(CONTROLLER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postData))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", CoreMatchers.containsString("Product name can't be blank")));

        // product name can't be empty
        postData = new ProductTypeVO("", "some descr", 1, new ProductTypeSpecificationVO[0]);
        mockMvc.perform(post(CONTROLLER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postData))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", CoreMatchers.containsString("Product name can't be blank")));

        // product name can't be blank
        postData = new ProductTypeVO("\t\n \r", "some descr", 1, new ProductTypeSpecificationVO[0]);
        mockMvc.perform(post(CONTROLLER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postData))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", CoreMatchers.containsString("Product name can't be blank")));

        // product description can't be null
        postData = new ProductTypeVO("some name", null, 1, new ProductTypeSpecificationVO[0]);
        mockMvc.perform(post(CONTROLLER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postData))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", CoreMatchers.containsString("Product description can't be blank")));

        // product description can't be empty
        postData = new ProductTypeVO("some name", "", 1, new ProductTypeSpecificationVO[0]);
        mockMvc.perform(post(CONTROLLER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postData))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", CoreMatchers.containsString("Product description can't be blank")));

        // product description can't be blank
        postData = new ProductTypeVO("some name", "\t\n \r", 1, new ProductTypeSpecificationVO[0]);
        mockMvc.perform(post(CONTROLLER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postData))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", CoreMatchers.containsString("Product description can't be blank")));

        // category id can't be negative
        postData = new ProductTypeVO("some name", "descr", -1, new ProductTypeSpecificationVO[0]);
        mockMvc.perform(post(CONTROLLER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postData))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", CoreMatchers.containsString("Category id must be a positive integer")));

        // category id can't be 0
        postData = new ProductTypeVO("some name", "descr", 0, new ProductTypeSpecificationVO[0]);
        mockMvc.perform(post(CONTROLLER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postData))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", CoreMatchers.containsString("Category id must be a positive integer")));

        // product type specifications can't be null
        postData = new ProductTypeVO("some name", "descr", 1, null);
        mockMvc.perform(post(CONTROLLER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postData))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", CoreMatchers.containsString("Product type specification must be supplied")));

        // whole object is invalid, shows all invalid fields
        postData = new ProductTypeVO("", null, -10, null);
        mockMvc.perform(post(CONTROLLER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postData))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", CoreMatchers.allOf(
                        CoreMatchers.containsString("Product name can't be blank"),
                        CoreMatchers.containsString("Product description can't be blank"),
                        CoreMatchers.containsString("Category id must be a positive integer"),
                        CoreMatchers.containsString("Product type specification must be supplied")
                )));

        verify(productTypeService, times(0))
                .save(any(ProductType.class));
    }

    @Test
    public void postProductType_ConsumesJson() throws Exception {
        ProductTypeVO postData = DummyTestData.PRODUCT_TYPE_VO();

        OAuth2AccessToken auth = createToken("william", "hispass");
        // If auth is null, that only means that the test used an non-existent user
        String authToken = auth.getValue();

        // can accept JSON
        mockMvc.perform(post(CONTROLLER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postData))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isCreated())
                .andExpect(header().doesNotExist(HttpHeaders.CONTENT_TYPE));

        // can accept JSON UTF-8
        mockMvc.perform(post(CONTROLLER_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(postData))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isCreated())
                .andExpect(header().doesNotExist(HttpHeaders.CONTENT_TYPE));
    }

    @Test
    public void postProductType_CanNotConsumeXml() throws Exception {
        ProductTypeVO postData = DummyTestData.PRODUCT_TYPE_VO();

        OAuth2AccessToken auth = createToken("william", "hispass");
        // If auth is null, that only means that the test used an non-existent user
        String authToken = auth.getValue();

        // can't accept XML
        mockMvc.perform(post(CONTROLLER_URI)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .content(objectMapper.writeValueAsString(postData))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void postProductType_CanNotConsumeAll() throws Exception {
        ProductTypeVO postData = DummyTestData.PRODUCT_TYPE_VO();

        OAuth2AccessToken auth = createToken("william", "hispass");
        // If auth is null, that only means that the test used an non-existent user
        String authToken = auth.getValue();

        // can't consume */*
        mockMvc.perform(post(CONTROLLER_URI)
                .contentType(MediaType.ALL)
                .content(objectMapper.writeValueAsString(postData))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void postProductType_NoContentType() throws Exception {
        ProductTypeVO postData = DummyTestData.PRODUCT_TYPE_VO();

        OAuth2AccessToken auth = createToken("william", "hispass");
        // If auth is null, that only means that the test used an non-existent user
        String authToken = auth.getValue();

        // no content type
        mockMvc.perform(post(CONTROLLER_URI)
                .content(objectMapper.writeValueAsString(postData))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void canRetrieveNewlyCreatedProductType() throws Exception {
        ProductTypeVO postData = DummyTestData.PRODUCT_TYPE_VO();
        when(productTypeService.findByName(postData.getName()))
                .thenReturn(Optional.of(DummyTestData.PRODUCT_TYPE_FROM_VO()));

        OAuth2AccessToken auth = createToken("william", "hispass");
        // If auth is null, that only means that the test used an non-existent user
        String authToken = auth.getValue();

        mockMvc.perform(post(CONTROLLER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postData))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isCreated())
                .andDo(result -> {
                    String newResource = result.getResponse().getHeader(HttpHeaders.LOCATION);
                    newResource = UriUtils.decode(newResource, UTF_8);
                    mockMvc.perform(get(newResource))
                            .andExpect(status().isOk())
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                            .andExpect(jsonPath("$.name").value(postData.getName()))
                            // TODO: 23-Jul-18 Assert that category is not null and stuff like that
                            .andExpect(jsonPath("$.description").value(postData.getDescription()))
                            .andExpect(jsonPath("$.specifications").isArray());

                    verify(productTypeService, times(1))
                            .findByName(postData.getName());
                });
    }
}