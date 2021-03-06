package com.seq.api;

import com.seq.exception.*;
import com.seq.http.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

import java.util.*;

/**
 * A taxonomy used to differentiate different types of tokens in a ledger.
 */
public class Flavor {
  /**
   * Unique, user-specified identifier.
   */
  @Expose
  public String id;

  /**
   * The list of IDs for the keys that control the flavor.
   */
  @SerializedName("key_ids")
  @Expose
  public List<String> keyIds;

  /**
   * The number of keys required to sign transactions that issue tokens of the
   * flavor.
   */
  @Expose
  public int quorum;

  /**
   * User-specified key-value data describing the flavor.
   */
  @Expose
  public Map<String, Object> tags;

  /**
   * A single page of flavors returned from a query.
   */
  public static class Page extends BasePage<Flavor> {}

  /**
   * Iterable interface for consuming individual flavors from a query.
   */
  public static class ItemIterable extends BaseItemIterable<Flavor> {
    public ItemIterable(Client client, String path, Query nextQuery) {
      super(client, path, nextQuery, Page.class);
    }
  }

  /**
   * A builder class for listing flavors in the ledger.
   */
  public static class ListBuilder extends BaseQueryBuilder<ListBuilder> {
    /**
     * Executes the query, returning a page of flavors that match the query.
     * @param client ledger API connection object
     * @return a page of flavors
     * @throws ChainException
     */
    public Page getPage(Client client) throws ChainException {
      return client.request("list-flavors", this.next, Page.class);
    }

    /**
     * Executes the query, returning a page of flavors that match the query
     * beginning with provided cursor.
     * @param client ledger API connection object
     * @param cursor string representing encoded query object
     * @return a page of flavors
     * @throws ChainException
     */
    public Page getPage(Client client, String cursor) throws ChainException {
      Query next = new Query();
      next.cursor = cursor;
      return client.request("list-flavors", next, Page.class);
    }

    /**
     * Executes the query, returning an iterable over flavors that match the query
     * @param client ledger API connection object
     * @return an iterable over flavors
     * @throws ChainException
     */
    public ItemIterable getIterable(Client client) throws ChainException {
      return new ItemIterable(client, "list-flavors", this.next);
    }
  }

  /**
   * A builder for defining flavors in the ledger.
   */
  public static class Builder {
    @Expose
    private String id;

    @Expose
    private Map<String, Object> tags;

    @SerializedName("key_ids")
    @Expose
    private List<String> keyIds;

    @Expose
    private Integer quorum;

    public Builder() {
      this.keyIds = new ArrayList<>();
    }

    /**
     * Creates a new flavor in the ledger.
     * @param client ledger API connection object
     * @return a flavor
     * @throws ChainException
     */
    public Flavor create(Client client) throws ChainException {
      return client.request("create-flavor", this, Flavor.class);
    }

    /**
     * Specifies the id for the new flavor.
     * @param id a unique, user-specified identifier
     * @return updated builder
     */
    public Builder setId(String id) {
      this.id = id;
      return this;
    }

    /**
     * Adds a key-value pair to the flavor's tags.
     * @param key key of the tag
     * @param value value of the tag
     * @return updated builder
     */
    public Builder addTag(String key, Object value) {
      if (this.tags == null) {
        this.tags = new HashMap<>();
      }
      this.tags.put(key, value);
      return this;
    }

    /**
     * Specifies key-value data that describes the flavor.
     * @param tags map of tag keys to tag values
     * @return updated builder
     */
    public Builder setTags(Map<String, Object> tags) {
      this.tags = tags;
      return this;
    }

    /**
     * Specifies the number of keys required to sign transactions that issue
     * tokens of the flavor. Defaults to the number of keys provided.
     * @param quorum a number less than or equal to the number of keys
     * @return updated builder
     */
    public Builder setQuorum(int quorum) {
      this.quorum = new Integer(quorum);
      return this;
    }

    /**
     * Adds a key that controls the flavor.
     * @param id the key's ID
     * @return updated builder
     */
    public Builder addKeyId(String id) {
      this.keyIds.add(id);
      return this;
    }
  }

  /**
   * A builder for updating a flavor's tags.
   */
  public static class TagUpdateBuilder {
    @Expose
    private String id;

    @Expose
    private Map<String, Object> tags;

    /**
     * Specifies the flavor to be updated.
     * @param id the flavor's ID
     * @return updated builder
     */
    public TagUpdateBuilder forId(String id) {
      this.id = id;
      return this;
    }

    /**
     * Specifies a new set of tags.
     * @param tags map of tag keys to tag values
     * @return updated builder
     */
    public TagUpdateBuilder setTags(Map<String, Object> tags) {
      this.tags = tags;
      return this;
    }

    /**
     * Updates the flavor's tags.
     * @param client ledger API connection object
     * @throws ChainException
     */
    public void update(Client client) throws ChainException {
      client.request("update-flavor-tags", this, SuccessMessage.class);
    }
  }
}
