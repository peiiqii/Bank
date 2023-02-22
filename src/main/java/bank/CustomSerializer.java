package bank;

import bank.exceptions.InterestExceptions;
import com.google.gson.*;
import java.lang.reflect.Type;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CustomSerializer implements JsonSerializer<Transaction>, JsonDeserializer<Transaction> {

    /**
     * custom deserializer for Transactions
     *
     * @param jsonElement
     * @param type
     * @param jsonDeserializationContext
     * @return specific Transaction
     * @throws JsonParseException
     */
    @Override
    public Transaction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jobj = jsonElement.getAsJsonObject();
        String classname = jobj.get("CLASSNAME").getAsString();

        JsonElement jelem = jobj.get("INSTANCE");

        return switch(classname){
            case "Payment" -> new Gson().fromJson(jelem, Payment.class);
            case "Transfer" -> new Gson().fromJson(jelem, Transfer.class);
            case "IncomingTransfer" -> new Gson().fromJson(jelem, IncomingTransfer.class);
            case "OutgoingTransfer" -> new Gson().fromJson(jelem, OutgoingTransfer.class);
            default -> throw new JsonParseException("Unbekannter Typ!");
        };
    }

    /**
     * custom serializer for accounts
     *
     * @param transaction
     * @param type
     * @param jsonSerializationContext
     * @return transaction as json
     */
    @Override
    public JsonElement serialize(Transaction transaction, Type type, JsonSerializationContext jsonSerializationContext) {

        JsonObject result = new JsonObject();
        JsonObject instance = new JsonObject(); //transaction information

        String classname = transaction.getClass().toString();
        String[] classParts = classname.split("\\."); // split to remove "class bank."
        classname = classParts[classParts.length-1]; // only select name of class
        result.addProperty("CLASSNAME", classname);

        if (transaction instanceof Transfer) {
            Transfer t = (Transfer) transaction;
            instance.addProperty("sender", t.getSender());
            instance.addProperty("recipient", t.getRecipient());
        }
        else if (transaction instanceof Payment){
            Payment p = (Payment) transaction;
            instance.addProperty("IncomingInterest", p.getIncomingInterest());
            instance.addProperty("OutgoingInterest", p.getOutgoingInterest());
        }

        instance.addProperty("date", transaction.getDate());
        instance.addProperty("amount", transaction.getAmount());
        instance.addProperty("description", transaction.getDescription());

        result.add("INSTANCE", instance);

        return result;
    }


}
