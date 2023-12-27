主要变化的如何从readObject到toString

不管什么链，jackson都要重写BaseJsonNode类，删除它的writeReplace方法

按照理论来说只需要寻找到继承`BaseJsonNode`的类，并且没有重写`toSting`方法，就能够替代`POJONode`类
