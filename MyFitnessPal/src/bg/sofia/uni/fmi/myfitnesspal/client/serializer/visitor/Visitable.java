package bg.sofia.uni.fmi.myfitnesspal.client.serializer.visitor;

public interface Visitable {
    void accept(ItemVisitor visitor);
}
