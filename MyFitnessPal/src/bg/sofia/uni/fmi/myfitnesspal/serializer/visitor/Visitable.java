package bg.sofia.uni.fmi.myfitnesspal.serializer.visitor;

public interface Visitable {
    void accept(ItemVisitor visitor);
}
